package com.yuanxiaoer.yuanxiaoer.ic_audit_application.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import com.yuanxiaoer.yuanxiaoer.ic_login.mapper.InsuranceCompanyMapper;
import com.yuanxiaoer.yuanxiaoer.ic_audit_application.entity.ICAuditApplication;
import com.yuanxiaoer.yuanxiaoer.ic_audit_application.mapper.ICAuditApplicationMapper;
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

@Slf4j
@Service
public class ICAuditApplicationServiceImpl extends ServiceImpl<ICAuditApplicationMapper, ICAuditApplication> implements ICAuditApplicationService {
    
    @Value("${upload.base-path}")
    private String uploadBasePath;

    @Autowired
    private InsuranceCompanyMapper insuranceCompanyMapper;
    @Autowired
    private ICAuditApplicationMapper applicationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)  // 事务注解，任何异常都回滚
    public String submitApplication(ICAuditApplication application, MultipartFile sealFile, HttpSession session) {
        // 1. 从session获取当前代理商信息
        InsuranceCompany loginInsuranceCompany = (InsuranceCompany) session.getAttribute("loginIC");
        if (loginInsuranceCompany == null) {
            return "未登录，请重新登录";
        }
        Long insuranceCompanyId = loginInsuranceCompany.getId();

        // 2. 检查是否已提交申请
        if (hasApplied(insuranceCompanyId)) {
            return "您已提交过申请，不可重复提交";
        }

        // 3. 生成唯一申请ID
        String applyId = "APPLY_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        application.setApplyId(applyId);
        application.setInsuranceCompanyId(insuranceCompanyId);

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

        // 6. 更新 insuranceCompany 表的 audit_status 为 1（待审核）
        int updated = insuranceCompanyMapper.updateAuditStatus(insuranceCompanyId, 1);
        if (updated != 1) {
            // 如果更新失败（比如 agency 记录被删除），抛出异常触发事务回滚
            throw new RuntimeException("更新代理商审核状态失败");
        }
        // 7. 更新session（刷新申请状态）
        loginInsuranceCompany.setAuditStatus(1); // 待审核
        session.setAttribute("loginIC", loginInsuranceCompany);

        return "申请提交成功";
    }

    @Override
    public boolean hasApplied(Long insuranceCompanyId) {
        Integer count = baseMapper.countByInsuranceCompanyId(insuranceCompanyId);
        return count != null && count > 0;
    }

    /**
     * 根据ID查询申请表单详情
     * @param id 申请表ID
     * @return 申请表单数据（拼接文件完整路径）
     */
    @Override
    public ICAuditApplication getApplicationById(Long id) {
        // 1. 构建查询条件（排除软删除数据）
        LambdaQueryWrapper<ICAuditApplication> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ICAuditApplication::getId, id)
                   .eq(ICAuditApplication::getIsDeleted, 0);
        
        // 2. 从数据库查询数据
        ICAuditApplication application = applicationMapper.selectOne(queryWrapper);
        if (application == null) {
            return null; // 无数据返回null，由Controller层统一处理
        }

        // 3. 拼接文件完整访问路径（方便前端预览）
        if (application.getSealFilePath() != null && !application.getSealFilePath().isEmpty()) {
            application.setSealFilePath(uploadBasePath + application.getSealFilePath());
        }

        return application;
    }
}
