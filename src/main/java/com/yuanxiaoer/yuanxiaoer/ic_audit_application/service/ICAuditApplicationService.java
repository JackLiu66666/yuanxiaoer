package com.yuanxiaoer.yuanxiaoer.ic_audit_application.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.ic_audit_application.entity.ICAuditApplication;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

public interface ICAuditApplicationService extends IService<ICAuditApplication> {
    // 提交申请（含文件）
    String submitApplication(ICAuditApplication application, MultipartFile sealFile, HttpSession session);
    
    // 检查代理商是否已提交申请
    boolean hasApplied(Long insuranceCompanyId);

    //根据ID查询申请表单详情
    ICAuditApplication getApplicationById(Long id);

}
