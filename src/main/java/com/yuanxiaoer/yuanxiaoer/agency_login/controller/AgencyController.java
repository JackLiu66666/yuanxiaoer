package com.yuanxiaoer.yuanxiaoer.agency_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.agency_login.dto.AgencyLoginDTO;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.service.AgencyService;
import com.yuanxiaoer.yuanxiaoer.agency_login.vo.AgencyInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 代理商控制器（RESTful 风格）
 */
@Tag(name = "代理商登录", description = "代理商注册、登录、登出及首页接口")
@RestController
@RequestMapping("/agency")
public class AgencyController {

    @Resource
    private AgencyService agencyService;

    /**
     * 注册接口
     */
    @Operation(summary = "代理商注册", description = "新用户注册成为代理商")
    @PostMapping("/register")
    public Result<?> register(@RequestBody Agency agency) {
        return agencyService.register(agency);
    }

    /**
     * 登录接口
     */
    @Operation(summary = "代理商登录", description = "代理商通过账号密码登录系统")
    @PostMapping("/login")
    public Result<?> login(@RequestBody AgencyLoginDTO loginDTO, HttpSession session) {
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        Result<?> result = agencyService.login(loginKey, password);
        // 登录成功，存入 Session（auditStatus 字段不存储）
        if (result.getCode() == 200) {
            Agency agency = (Agency) result.getData();
            agency.setAuditStatus(null); // 将 auditStatus 置为 null，不存入 session
            session.setAttribute("loginAgency", agency);
            result.setMsg("登录成功，即将进入代理商界面"); // 重置提示语
        }
        return result;
    }

    /**
     * 代理商首页（登录后进入）
     */
    @Operation(summary = "代理商首页", description = "获取代理商登录后的首页欢迎信息")
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
    @Operation(summary = "代理商登出", description = "代理商退出登录，销毁 Session")
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("loginAgency"); // 移除登录态
        session.invalidate(); // 销毁Session
        return Result.success("退出登录成功");
    }

    /**
     * 获取当前代理商审核状态
     */
    @Operation(summary = "获取审核状态", description = "查询当前登录代理商的审核状态")
    @GetMapping("/getAgencyauditStatus")
    public Result<?> getAgencyauditStatus(HttpSession session) {
        Agency loginAgency = (Agency) session.getAttribute("loginAgency");
        if (loginAgency == null) {
            return Result.error("未登录或已过期");
        }
        
        Long agencyId = loginAgency.getId();
        Integer auditStatus = agencyService.getAuditStatusById(agencyId);
        
        if (auditStatus == null) {
            return Result.error("代理商不存在");
        }
        
        return Result.success(auditStatus);
    }

    /**
     * 获取当前代理商业务范围
     */
    @Operation(summary = "获取业务范围", description = "查询当前登录代理商的业务范围（JSON 数组）")
    @GetMapping("/getAgencyBusinessScope")
    public Result<?> getAgencyBusinessScope(HttpSession session) {
        Agency loginAgency = (Agency) session.getAttribute("loginAgency");
        if (loginAgency == null) {
            return Result.error("未登录或已过期");
        }
        
        Long agencyId = loginAgency.getId();
        String businessScope = agencyService.getBusinessScopeByAgencyId(agencyId);
        
        if (businessScope == null) {
            return Result.error("未找到代理商信息，请先申请入驻");
        }
        
        return Result.success(businessScope);
    }

    /**
     * 获取当前代理商详细信息
     */
    @Operation(summary = "获取代理商信息", description = "查询当前登录代理商的详细信息")
    @GetMapping("/getAgencyInfo")
    public Result<?> getAgencyInfo(HttpSession session) {
        Agency loginAgency = (Agency) session.getAttribute("loginAgency");
        if (loginAgency == null) {
            return Result.error("未登录或已过期");
        }
        
        Long agencyId = loginAgency.getId();
        AgencyInfoVO agencyInfoVO = agencyService.getAgencyInfoByAgencyId(agencyId);
        
        if (agencyInfoVO == null) {
            return Result.error("未找到代理商信息，请先申请入驻");
        }
        
        return Result.success(agencyInfoVO);
    }
}
