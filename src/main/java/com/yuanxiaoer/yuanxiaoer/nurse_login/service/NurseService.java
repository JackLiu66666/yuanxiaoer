package com.yuanxiaoer.yuanxiaoer.nurse_login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.nurse_login.entity.Nurse;
import com.yuanxiaoer.yuanxiaoer.common.Result;

/**
 * 护士Service
 */
public interface NurseService extends IService<Nurse> {
    /**
     * 护士注册
     */
    Result<?> register(Nurse nurse);

    /**
     * 护士登录
     */
    Result<?> login(String loginKey, String password);

    /**
     * 设置在线状态
     */
    Result<?> setOnlineStatus(Long id, Integer onlineStatus);
}