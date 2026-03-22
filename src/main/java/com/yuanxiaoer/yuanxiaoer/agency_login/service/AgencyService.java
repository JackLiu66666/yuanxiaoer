package com.yuanxiaoer.yuanxiaoer.agency_login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.vo.AgencyInfoVO;
import com.yuanxiaoer.yuanxiaoer.common.Result;

/**
 * 代理商 Service
 */
public interface AgencyService extends IService<Agency> {
    /**
     * 代理商注册
     */
    Result<?> register(Agency agency);

    /**
     * 代理商登录
     */
    Result<?> login(String loginKey, String password);

    /**
     * 根据 ID 获取审核状态
     */
    Integer getAuditStatusById(Long agencyId);

    /**
     * 根据代理商 ID 获取业务范围
     */
    String getBusinessScopeByAgencyId(Long agencyId);

    /**
     * 根据代理商 ID 获取详细信息
     */
    AgencyInfoVO getAgencyInfoByAgencyId(Long agencyId);
}
