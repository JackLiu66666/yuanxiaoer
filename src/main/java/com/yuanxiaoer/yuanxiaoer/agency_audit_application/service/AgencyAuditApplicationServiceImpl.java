package com.yuanxiaoer.yuanxiaoer.agency_audit_application.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.HospitalMapper;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.mapper.AgencyMapper;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper.AgencyAuditApplicationMapper;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.CityVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalSelectVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.ProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AgencyAuditApplicationServiceImpl extends ServiceImpl<AgencyAuditApplicationMapper, AgencyAuditApplication> 
        implements AgencyAuditApplicationService {

    @Value("${upload.base-path}")
    private String uploadBasePath;

    @Autowired
    private AgencyMapper agencyMapper;
    @Autowired
    private AgencyAuditApplicationMapper applicationMapper;
    @Autowired
    private HospitalMapper hospitalMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务注解，任何异常都回滚
    public String submitApplication(AgencyAuditApplication application, MultipartFile sealFile, HttpSession session) {
        // 1. 从session获取当前代理商信息
        Agency loginAgency = (Agency) session.getAttribute("loginAgency");
        if (loginAgency == null) {
            return "未登录，请重新登录";
        }
        Long agencyId = loginAgency.getId();

        // 2. 检查是否已提交申请
        if (hasApplied(agencyId)) {
            return "您已提交过申请，不可重复提交";
        }

        // 3. 生成唯一申请ID
        String applyId = "APPLY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        application.setApplyId(applyId);
        application.setAgencyId(agencyId);

        // 4. 处理文件上传
        try {
            // 4.1 创建申请专属文件夹
            String applyDir = uploadBasePath + applyId + "/";
            File dirFile = new File(applyDir);
            if (!dirFile.exists()) {
                FileUtils.forceMkdir(dirFile);
            }

            // 4.2 重命名文件（时间戳+原扩展名）
            String originalFilename = sealFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = "seal_" + System.currentTimeMillis() + ext;
            String filePath = applyDir + newFileName;

            // 4.3 保存文件
            File sealFileDest = new File(filePath);
            sealFile.transferTo(sealFileDest);

            // 4.4 保存文件路径到实体
            application.setSealFilePath(applyId + "/" + newFileName);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return "文件上传失败：" + e.getMessage();
        }

        // 5. 保存申请数据到数据库
        save(application);

        // 6. 更新 agency 表的 audit_status 为 1（待审核）
        int updated = agencyMapper.updateAuditStatus(agencyId, 1);
        if (updated != 1) {
            // 如果更新失败（比如 agency 记录被删除），抛出异常触发事务回滚
            throw new RuntimeException("更新代理商审核状态失败");
        }
        // 7. 更新session（刷新申请状态）
        loginAgency.setAuditStatus(1); // 待审核
        session.setAttribute("loginAgency", loginAgency);

        return "申请提交成功";
    }

    @Override
    public boolean hasApplied(Long agencyId) {
        Integer count = baseMapper.countByAgencyId(agencyId);
        return count != null && count > 0;
    }

    /**
     * 根据ID查询申请表单详情
     * @param id 申请表ID
     * @return 申请表单数据（拼接文件完整路径）
     */
    @Override
    public AgencyAuditApplication getApplicationById(Long id) {
        // 1. 构建查询条件（排除软删除数据）
        LambdaQueryWrapper<AgencyAuditApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgencyAuditApplication::getId, id)
                   .eq(AgencyAuditApplication::getIsDeleted, 0);
        
        // 2. 从数据库查询数据
        AgencyAuditApplication application = applicationMapper.selectOne(queryWrapper);
        if (application == null) {
            return null; // 无数据返回null，由Controller层统一处理
        }

        // 3. 拼接文件完整访问路径（方便前端预览）
        if (application.getSealFilePath() != null && !application.getSealFilePath().isEmpty()) {
            application.setSealFilePath(uploadBasePath + application.getSealFilePath());
        }

        return application;
    }

    @Override
    public List<ProvinceVO> getProvinces() {
        List<String> provinces = hospitalMapper.getAllProvinces();
        return provinces.stream()
                .map(province -> {
                    ProvinceVO vo = new ProvinceVO();
                    vo.setProvince(province);
                    return vo;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<CityVO> getCitiesByProvince(String province) {
        List<String> cities = hospitalMapper.getAllCitiesByProvince(province);
        return cities.stream()
                .map(city -> {
                    CityVO vo = new CityVO();
                    vo.setCity(city);
                    return vo;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<HospitalSelectVO> getHospitalsByCity(String city) {
        List<HospitalSelectVO> hospitals = hospitalMapper.getAllHospitalsByCity(city);
        return hospitals;
    }
}
