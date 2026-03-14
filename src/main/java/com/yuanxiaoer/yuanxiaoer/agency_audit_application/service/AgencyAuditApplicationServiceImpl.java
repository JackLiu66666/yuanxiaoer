package com.yuanxiaoer.yuanxiaoer.agency_audit_application.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.HospitalMapper;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.mapper.AgencyMapper;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper.AgencyAuditApplicationMapper;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.util.CooperationAreaItem;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            String applyDir = uploadBasePath + "AgencyApply/" + applyId + "/";
            File dirFile = new File(applyDir);
            if (!dirFile.exists()) {
                FileUtils.forceMkdir(dirFile);
            }

            // 4.2 重命名文件（时间戳 + 原扩展名）
            String originalFilename = sealFile.getOriginalFilename();
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFileName = "seal_" + System.currentTimeMillis() + ext;
            String filePath = applyDir + newFileName;

            // 4.3 保存文件
            File sealFileDest = new File(filePath);
            sealFile.transferTo(sealFileDest);

            // 4.4 保存文件路径到实体
            application.setSealFilePath("AgencyApply/" + applyId + "/" + newFileName);
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
     * 根据 ID 查询申请表单详情
     * @param id 申请表 ID
     * @return 申请表单数据（包含 Base64 编码的文件内容、业务名称列表、医院信息列表）
     */
    @Override
    public AgencyAuditApplication getApplicationById(Long id) {
        // 1. 从数据库查询数据（不再检查 is_deleted，改为检查 audit_status）
        LambdaQueryWrapper<AgencyAuditApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgencyAuditApplication::getId, id);
        
        // 2. 从数据库查询数据
        AgencyAuditApplication application = applicationMapper.selectOne(queryWrapper);
        if (application == null) {
            return null; // 无数据返回 null，由 Controller 层统一处理
        }

        // 3. 读取文件并转换为 Base64 编码
        if (application.getSealFilePath() != null && !application.getSealFilePath().isEmpty()) {
            try {
                String fullPath = uploadBasePath + application.getSealFilePath();
                File file = new File(fullPath);
                if (file.exists()) {
                    // 读取文件字节
                    byte[] fileBytes = FileUtils.readFileToByteArray(file);
                    // 转换为 Base64 编码
                    String base64Data = java.util.Base64.getEncoder().encodeToString(fileBytes);
                    // 获取文件扩展名
                    String ext = "";
                    int dotIndex = application.getSealFilePath().lastIndexOf(".");
                    if (dotIndex > 0) {
                        ext = application.getSealFilePath().substring(dotIndex + 1).toLowerCase();
                    }
                    // 设置 MIME 类型
                    String mimeType = "image/jpeg"; // 默认
                    if ("png".equals(ext)) {
                        mimeType = "image/png";
                    } else if ("gif".equals(ext)) {
                        mimeType = "image/gif";
                    } else if ("bmp".equals(ext)) {
                        mimeType = "image/bmp";
                    } else if ("webp".equals(ext)) {
                        mimeType = "image/webp";
                    }
                    // 封装为 Data URL 格式：data:image/jpeg;base64,/9j/4AAQSk...
                    application.setSealFilePath("data:" + mimeType + ";base64," + base64Data);
                }
            } catch (IOException e) {
                log.error("读取文件失败：" + application.getSealFilePath(), e);
                // 文件读取失败时，保留原路径或设置为错误提示
                application.setSealFilePath("文件读取失败");
            }
        }

        // 4. 处理 businessScope：将代码列表转换为业务名称列表
        if (application.getBusinessScope() != null && !application.getBusinessScope().isEmpty()) {
            try {
                List<Integer> codes = JSON.parseArray(application.getBusinessScope(), Integer.class);
                List<String> businessNames = new ArrayList<>();
                for (Integer code : codes) {
                    businessNames.add(com.yuanxiaoer.yuanxiaoer.agency_audit_application.util.BusinessScopeEnum.getName(code));
                }
                // 将业务名称列表转为 JSON 字符串存储回字段
                application.setBusinessScope(JSON.toJSONString(businessNames));
            } catch (Exception e) {
                log.error("解析业务范围失败", e);
                application.setBusinessScope("[]");
            }
        }

        // 5. 处理 cooperationArea：将医院 ID 列表转换为医院详细信息列表
        if (application.getCooperationArea() != null && !application.getCooperationArea().isEmpty()) {
            try {
                // 解析合作区域 JSON
                List<Map<String, Object>> areas = JSON.parseObject(
                    application.getCooperationArea(), 
                    new com.alibaba.fastjson.TypeReference<List<Map<String, Object>>>() {}
                );
                
                // 遍历每个区域，替换 hospitalIds 为医院详细信息
                for (Map<String, Object> area : areas) {
                    Object hospitalIdsObj = area.get("hospitalIds");
                    if (hospitalIdsObj instanceof List) {
                        // 安全转换：先转为 List<?>，再逐个转换为 Long
                        List<?> rawList = (List<?>) hospitalIdsObj;
                        List<Long> hospitalIds = rawList.stream()
                            .map(obj -> {
                                if (obj instanceof Number) {
                                    return ((Number) obj).longValue();
                                } else if (obj instanceof String) {
                                    return Long.parseLong((String) obj);
                                } else {
                                    throw new IllegalArgumentException("Invalid hospitalId type: " + obj);
                                }
                            })
                            .collect(Collectors.toList());
                        
                        if (!hospitalIds.isEmpty()) {
                            // 根据医院 ID 列表查询医院详细信息
                            List<com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalInfoVO> hospitals = 
                                hospitalMapper.getHospitalsByIds(hospitalIds);
                            // 替换 hospitalIds 为医院详细信息列表
                            area.put("hospitals", hospitals);
                            area.remove("hospitalIds");
                        } else {
                            // 空数组表示全市，保留空数组或设为 null
                            area.put("hospitals", new ArrayList<>());
                            area.remove("hospitalIds");
                        }
                    }
                }
                
                // 将处理后的合作区域转回 JSON 字符串
                application.setCooperationArea(JSON.toJSONString(areas));
            } catch (Exception e) {
                log.error("解析合作区域失败", e);
                application.setCooperationArea("[]");
            }
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

    @Override
    public boolean validateBusinessScope(List<Integer> codes) {
        if (codes == null || codes.isEmpty()) {
            return false;
        }
        
        // 验证每个代码是否在 1-6 范围内
        for (Integer code : codes) {
            if (code < 1 || code > 6) {
                log.warn("无效的业务范围代码：{}", code);
                return false;
            }
        }
        
        // 检查是否有重复
        if (codes.size() != codes.stream().distinct().count()) {
            log.warn("业务范围代码有重复");
            return false;
        }
        
        return true;
    }
    
    /**
     * 验证合作区域数据是否有效
     * @param items 合作区域列表
     * @return 验证结果
     */
    @Override
    public boolean validateCooperationArea(List<CooperationAreaItem> items) {
        if (items == null || items.isEmpty()) {
            return false;
        }
        
        for (CooperationAreaItem item : items) {
            // 验证省份和城市不能为空
            if (item.getProvince() == null || item.getProvince().trim().isEmpty()) {
                log.warn("合作区域缺少省份信息");
                return false;
            }
            if (item.getCity() == null || item.getCity().trim().isEmpty()) {
                log.warn("合作区域缺少城市信息");
                return false;
            }
            
            // 如果指定了医院 IDs，需要验证
            if (item.getHospitalIds() != null && !item.getHospitalIds().isEmpty()) {
                // 验证每个医院 ID 是否为正数
                for (Long hospitalId : item.getHospitalIds()) {
                    if (hospitalId == null || hospitalId <= 0) {
                        log.warn("无效的医院 ID: {}", hospitalId);
                        return false;
                    }
                }
                
                // 检查是否有重复的医院 ID
                if (item.getHospitalIds().size() != item.getHospitalIds().stream().distinct().count()) {
                    log.warn("城市 {} 中有重复的医院 ID", item.getCity());
                    return false;
                }
                
                // TODO: 可以在这里添加医院存在性验证
                log.info("验证城市 {} 的医院 IDs: {}", item.getCity(), item.getHospitalIds());
            }
            // 如果 hospitalIds 为空或 null，表示全市，不需要验证医院 ID
        }
        
        return true;
    }
}
