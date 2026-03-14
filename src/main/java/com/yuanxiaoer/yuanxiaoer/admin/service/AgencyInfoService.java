package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyInfo;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;

/**
 * 代理商信息服务接口
 */
public interface AgencyInfoService extends IService<AgencyInfo> {
    
    /**
     * 根据申请信息创建代理商信息（审核通过时调用）
     * @param application 申请表单数据
     * @param sealFileBasePath 盖章文件存储的基础路径（用于复制文件）
     * @return 是否创建成功
     */
    boolean createFromApplication(AgencyAuditApplication application, String sealFileBasePath);
}
