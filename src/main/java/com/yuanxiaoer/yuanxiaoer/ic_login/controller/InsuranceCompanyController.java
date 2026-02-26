package com.yuanxiaoer.yuanxiaoer.ic_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.ic_login.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import com.yuanxiaoer.yuanxiaoer.ic_login.service.InsuranceCompanyService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 保险公司控制器
 */
@RestController
@RequestMapping("/insurance_company")
public class InsuranceCompanyController {

    @Resource
    private InsuranceCompanyService insuranceCompanyService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody InsuranceCompany insuranceCompany) {
        return insuranceCompanyService.register(insuranceCompany);
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        Result<?> result = insuranceCompanyService.login(loginKey, password);
        // 登录成功，存入Session
        if (result.getCode() == 200) {
            InsuranceCompany insuranceCompany = (InsuranceCompany) result.getData();
            session.setAttribute("loginIC", insuranceCompany); // 存入登录用户
            result.setMsg("登录成功，即将进入代理商界面"); // 重置提示语
        }
        return result;
    }

    /**
     * 保险公司首页（登录后进入）
     */
    @GetMapping("/index")
    public Result<?> index(HttpSession session) {
        InsuranceCompany loginInsuranceCompany = (InsuranceCompany) session.getAttribute("loginIC");
         // 返回包含用户信息的对象
        Map<String, Object> data = new HashMap<>();
        data.put("welcome", "欢迎 " + loginInsuranceCompany.getUsername() + " 进入代理商界面");
        data.put("InsuranceCompany", loginInsuranceCompany); // 返回用户信息
        return Result.success(data);
    }

    /**
     * 退出登录接口
     */
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("loginIC"); // 移除登录态
        session.invalidate(); // 销毁Session
        return Result.success("退出登录成功");
    }
}
