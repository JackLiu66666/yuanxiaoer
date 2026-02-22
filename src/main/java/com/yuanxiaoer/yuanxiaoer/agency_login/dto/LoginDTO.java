package com.yuanxiaoer.yuanxiaoer.agency_login.dto;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    /**
     * 管理员账号
     */
    private String loginKey;
    /**
     * 密码
     */
    private String password;
}
