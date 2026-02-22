package com.yuanxiaoer.yuanxiaoer.agency_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.agency_login.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.service.AgencyService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

/**
 * 代理商控制器（RESTful风格）
 */
@RestController
@RequestMapping("/agency")
public class AgencyController {

    @Resource
    private AgencyService agencyService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody Agency agency) {
        return agencyService.register(agency);
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        return agencyService.login(loginKey, password);
    }

    /**
     * 代理商首页（登录后进入）
     */
    @GetMapping("/index")
    public Result<?> index() {
        return Result.success("欢迎进入代理商界面，该界面待开发...");
    }
}