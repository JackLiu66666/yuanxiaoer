package com.yuanxiaoer.yuanxiaoer.agency_audit_application.service;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper.AgencyAuditApplicationMapper;
//import com.yuanxiaoer.yuanxiaoer.agency_audit_application.service.AgencyAuditApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AgencyAuditApplicationServiceImpl extends ServiceImpl<AgencyAuditApplicationMapper, AgencyAuditApplication> 
        implements AgencyAuditApplicationService {

    @Value("${upload.base-path}")
    private String uploadBasePath;

    @Override
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

        // 6. 更新session（刷新申请状态）
        loginAgency.setAuditStatus(1); // 待审核
        session.setAttribute("loginAgency", loginAgency);

        return "申请提交成功";
    }

    @Override
    public boolean hasApplied(Long agencyId) {
        Integer count = baseMapper.countByAgencyId(agencyId);
        return count != null && count > 0;
    }
}
