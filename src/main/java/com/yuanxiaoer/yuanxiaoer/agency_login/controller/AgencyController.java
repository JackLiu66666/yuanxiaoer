package com.yuanxiaoer.yuanxiaoer.agency_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.agency_login.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.service.AgencyService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

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
    public Result<?> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        Result<?> result = agencyService.login(loginKey, password);
        // 登录成功，存入Session
        if (result.getCode() == 200) {
            Agency agency = (Agency) result.getData();
            session.setAttribute("loginAgency", agency); // 存入登录用户
            result.setMsg("登录成功，即将进入代理商界面"); // 重置提示语
        }
        return result;
    }

    /**
     * 代理商首页（登录后进入）
     */
    @GetMapping("/index")
    public Result<?> index(HttpSession session) {
        Agency loginAgency = (Agency) session.getAttribute("loginAgency");
         // 返回包含用户信息的对象
        Map<String, Object> data = new HashMap<>();
        data.put("welcome", "欢迎 " + loginAgency.getUsername() + " 进入代理商界面");
        data.put("agency", loginAgency); // 返回用户信息
        return Result.success(data);
    }

    /**
     * 退出登录接口
     */
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("loginAgency"); // 移除登录态
        session.invalidate(); // 销毁Session
        return Result.success("退出登录成功");
    }
}