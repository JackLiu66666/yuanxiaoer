package com.yuanxiaoer.yuanxiaoer.ic_login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import com.yuanxiaoer.yuanxiaoer.common.Result;

/**
 * 保险公司Service
 */
public interface InsuranceCompanyService extends IService<InsuranceCompany> {
    /**
     * 保险公司注册
     */
    Result<?> register(InsuranceCompany insuranceCompany);

    /**
     * 保险公司登录
     */
    Result<?> login(String loginKey, String password);
}
