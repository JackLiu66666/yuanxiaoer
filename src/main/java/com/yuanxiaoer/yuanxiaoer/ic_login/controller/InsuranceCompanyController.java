package com.yuanxiaoer.yuanxiaoer.ic_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.ic_login.dto.ICLoginDTO;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import com.yuanxiaoer.yuanxiaoer.ic_login.service.InsuranceCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 保险公司控制器
 */
@Tag(name = "保险公司登录", description = "保险公司注册、登录、登出及首页接口")
@RestController
@RequestMapping("/insurance_company")
public class InsuranceCompanyController {

    @Resource
    private InsuranceCompanyService insuranceCompanyService;

    /**
     * 注册接口
     */
    @Operation(summary = "保险公司注册", description = "新用户注册成为保险公司")
    @PostMapping("/register")
    public Result<?> register(@RequestBody InsuranceCompany insuranceCompany) {
        return insuranceCompanyService.register(insuranceCompany);
    }

    /**
     * 登录接口
     */
    @Operation(summary = "保险公司登录", description = "保险公司通过账号密码登录系统")
    @PostMapping("/login")
    public Result<?> login(@RequestBody ICLoginDTO loginDTO, HttpSession session) {
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
    @Operation(summary = "保险公司首页", description = "获取保险公司登录后的首页欢迎信息")
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
    @Operation(summary = "保险公司登出", description = "保险公司退出登录，销毁 Session")
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("loginIC"); // 移除登录态
        session.invalidate(); // 销毁Session
        return Result.success("退出登录成功");
    }
}
