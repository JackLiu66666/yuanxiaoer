package com.yuanxiaoer.yuanxiaoer.nurse_login.dto;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    /**
     * 护士账号loginKey
     */
    private String loginKey;
    /**
     * 密码
     */
    private String password;
}