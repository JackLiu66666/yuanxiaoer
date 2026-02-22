package com.yuanxiaoer.yuanxiaoer.agency_login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.common.Result;

/**
 * 代理商Service
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
}
