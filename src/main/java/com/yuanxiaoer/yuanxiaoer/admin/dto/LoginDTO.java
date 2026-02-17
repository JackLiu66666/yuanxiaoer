package com.yuanxiaoer.yuanxiaoer.admin.dto;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {
    /**
     * 管理员账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
