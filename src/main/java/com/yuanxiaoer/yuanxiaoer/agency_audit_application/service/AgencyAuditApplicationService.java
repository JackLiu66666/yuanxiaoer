package com.yuanxiaoer.yuanxiaoer.agency_audit_application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

public interface AgencyAuditApplicationService extends IService<AgencyAuditApplication> {
    // 提交申请（含文件）
    String submitApplication(AgencyAuditApplication application, MultipartFile sealFile, HttpSession session);
    
    // 检查代理商是否已提交申请
    boolean hasApplied(Long agencyId);

    //根据ID查询申请表单详情
    AgencyAuditApplication getApplicationById(Long id);
}