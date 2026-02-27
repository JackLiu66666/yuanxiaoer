package com.yuanxiaoer.yuanxiaoer.nurse_login.controller;

import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.nurse_login.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.nurse_login.entity.Nurse;
import com.yuanxiaoer.yuanxiaoer.nurse_login.service.NurseService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;

/**
 * 护士控制器
 */
@RestController
@RequestMapping("/nurse")
public class NurseController {

    @Resource
    private NurseService nurseService;

    /**
     * 注册接口
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody Nurse nurse) {
        return nurseService.register(nurse);
    }

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        String loginKey = loginDTO.getLoginKey();
        String password = loginDTO.getPassword();
        Result<?> result = nurseService.login(loginKey, password);
        // 登录成功，存入Session
        if (result.getCode() == 200) {
            Nurse nurse = (Nurse) result.getData();
            session.setAttribute("loginNurse", nurse); // 存入登录用户
            result.setMsg("登录成功，即将进入护士界面"); // 重置提示语
        }
        return result;
    }

    /**
     * 护士首页（登录后进入）
     */
    @GetMapping("/index")
    public Result<?> index(HttpSession session) {
        Nurse loginNurse = (Nurse) session.getAttribute("loginNurse");
        // 返回包含用户信息的对象
        Map<String, Object> data = new HashMap<>();
        data.put("welcome", "欢迎 " + loginNurse.getUsername() + " 进入护士界面");
        data.put("nurse", loginNurse); // 返回用户信息
        return Result.success(data);
    }

    /**
     * 检查审核状态接口
     * 返回审核状态码：0-未审核 1-待审核 2-通过 3-不通过
     */
    @GetMapping("/check_audit_status")
    public Result<?> checkAuditStatus(HttpSession session) {
        Nurse loginNurse = (Nurse) session.getAttribute("loginNurse");
        if (loginNurse == null) {
            return Result.error("用户未登录");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("auditStatus", loginNurse.getAuditStatus());
        data.put("statusMessage", getAuditStatusMessage(loginNurse.getAuditStatus()));
        
        return Result.success(data);
    }

    /**
     * 设置在线状态接口
     */
    @PostMapping("/set_online_status")
    public Result<?> setOnlineStatus(@RequestParam Long id, @RequestParam Integer onlineStatus) {
        return nurseService.setOnlineStatus(id, onlineStatus);
    }

    /**
     * 退出登录接口
     */
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        Nurse loginNurse = (Nurse) session.getAttribute("loginNurse");
        if (loginNurse != null) {
            // 设置为离线状态
            nurseService.setOnlineStatus(loginNurse.getId(), 0);
        }
        session.removeAttribute("loginNurse"); // 移除登录态
        session.invalidate(); // 销毁Session
        return Result.success("退出登录成功");
    }

    /**
     * 根据审核状态码返回对应的消息
     */
    private String getAuditStatusMessage(Integer auditStatus) {
        switch (auditStatus) {
            case 0: return "未审核";
            case 1: return "待审核";
            case 2: return "审核通过";
            case 3: return "审核不通过";
            default: return "未知状态";
        }
    }
}