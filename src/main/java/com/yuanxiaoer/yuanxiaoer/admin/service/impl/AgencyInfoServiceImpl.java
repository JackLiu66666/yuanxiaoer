package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyInfo;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.AgencyInfoMapper;
import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyInfoService;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

/**
 * 代理商信息服务实现类
 */
@Slf4j
@Service
public class AgencyInfoServiceImpl extends ServiceImpl<AgencyInfoMapper, AgencyInfo> implements AgencyInfoService {

    @Value("${upload.base-path}")
    private String uploadBasePath;

    /**
     * 根据申请信息创建代理商信息（审核通过时调用）
     * @param application 申请表单数据
     * @param sealFileBasePath 盖章文件存储的基础路径（用于复制文件）
     * @return 是否创建成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createFromApplication(AgencyAuditApplication application, String sealFileBasePath) {
        try {
            // 1. 检查是否已存在该代理商的信息
            LambdaQueryWrapper<AgencyInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AgencyInfo::getAgencyId, application.getAgencyId());
            Long count = this.count(queryWrapper);
            
            if (count > 0) {
                log.warn("代理商 ID {} 已存在信息记录，跳过创建", application.getAgencyId());
                return true; // 已存在则返回成功
            }
            
            // 2. 创建新的代理商信息
            AgencyInfo agencyInfo = new AgencyInfo();
            agencyInfo.setAgencyId(application.getAgencyId());
            agencyInfo.setAgencyFullName(application.getAgencyFullName());
            agencyInfo.setSocialCreditCode(application.getSocialCreditCode());
            agencyInfo.setRegisteredAddress(application.getRegisteredAddress());
            agencyInfo.setAgencyType(application.getAgencyType());
            agencyInfo.setBusinessLicenseNo(application.getBusinessLicenseNo());
            agencyInfo.setLicenseValidStart(application.getLicenseValidStart());
            agencyInfo.setLicenseValidEnd(application.getLicenseValidEnd());
            agencyInfo.setLegalPersonName(application.getLegalPersonName());
            agencyInfo.setLegalPersonIdCard(application.getLegalPersonIdCard());
            agencyInfo.setLegalPersonPhone(application.getLegalPersonPhone());
            agencyInfo.setContactPersonName(application.getContactPersonName());
            agencyInfo.setContactPersonPosition(application.getContactPersonPosition());
            agencyInfo.setContactPersonPhone(application.getContactPersonPhone());
            agencyInfo.setContactPersonOfficePhone(application.getContactPersonOfficePhone());
            agencyInfo.setContactPersonEmail(application.getContactPersonEmail());
            agencyInfo.setCompanyFixedPhone(application.getCompanyFixedPhone());
            agencyInfo.setBusinessScope(application.getBusinessScope());
            agencyInfo.setCooperationArea(application.getCooperationArea());
            agencyInfo.setCooperationMode(application.getCooperationMode());
            agencyInfo.setAuthenticityCommitment(application.getAuthenticityCommitment());
            agencyInfo.setApplyDate(application.getApplyDate());
            agencyInfo.setAuditStatus(2); // 审核通过
            
            // 3. 处理盖章文件复制
            if (application.getSealFilePath() != null && !application.getSealFilePath().isEmpty()) {
                try {
                    // 为代理商创建专属文件夹（使用 agency_id 作为文件夹名）
                    String agencyDir = uploadBasePath + "AgencyInfo/" + application.getAgencyId() + "/";
                    File dirFile = new File(agencyDir);
                    if (!dirFile.exists()) {
                        FileUtils.forceMkdir(dirFile);
                    }
                    
                    // 获取原文件的完整路径
                    String oldFullPath = sealFileBasePath + application.getSealFilePath();
                    File oldFile = new File(oldFullPath);
                    
                    if (oldFile.exists()) {
                        // 获取文件扩展名
                        String ext = "";
                        int dotIndex = application.getSealFilePath().lastIndexOf(".");
                        if (dotIndex > 0) {
                            ext = application.getSealFilePath().substring(dotIndex);
                        }
                        
                        // 生成新文件名（使用时间戳避免重名）
                        String newFileName = "seal_" + System.currentTimeMillis() + ext;
                        String newFilePath = agencyDir + newFileName;
                        
                        // 复制文件到新目录
                        FileUtils.copyFile(oldFile, new File(newFilePath));
                        
                        // 保存相对路径（相对于 upload.base-path）
                        agencyInfo.setSealFilePath("AgencyInfo/" + application.getAgencyId() + "/" + newFileName);
                        
                        log.info("已复制盖章文件到：{}", newFilePath);
                    } else {
                        log.warn("原盖章文件不存在：{}", oldFullPath);
                    }
                } catch (IOException e) {
                    log.error("复制盖章文件失败", e);
                    throw new RuntimeException("复制盖章文件失败：" + e.getMessage(), e);
                }
            }
            
            // 4. 保存到数据库
            boolean saved = this.save(agencyInfo);
            
            if (saved) {
                log.info("代理商信息创建成功，agencyId: {}", application.getAgencyId());
                return true;
            } else {
                log.error("代理商信息保存失败，agencyId: {}", application.getAgencyId());
                return false;
            }
            
        } catch (Exception e) {
            log.error("创建代理商信息失败", e);
            throw new RuntimeException("创建代理商信息失败：" + e.getMessage(), e);
        }
    }
}
