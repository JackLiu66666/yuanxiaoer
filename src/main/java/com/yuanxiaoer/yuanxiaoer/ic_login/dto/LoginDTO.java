package com.yuanxiaoer.yuanxiaoer.ic_login.dto;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    /**
     * 代理商账号loginKey
     */
    private String loginKey;
    /**
     * 密码
     */
    private String password;
}
