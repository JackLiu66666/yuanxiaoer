package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.yuanxiaoer.yuanxiaoer.admin.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.service.SysAdminService;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.service.AgencyAuditApplicationService;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
public class SysAdminController {

    private final SysAdminService sysAdminService;

    @Resource
    private AgencyAuditApplicationService applicationService;

    // 构造器注入
    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    /**
     * 登录接口
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<SysAdmin> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        Result<SysAdmin> result = sysAdminService.login(loginDTO);
        if (result.getCode() == 200) {
            session.setAttribute("loginAdmin", result.getData());
        }
        return result;
    }

    /**
     * 根据ID查询申请表单详情
     * @param id 申请表ID
     * @param session 会话（验证登录）
     * @return 表单数据
     */
    @GetMapping("/agencyApplicationReview/{id}")
    public Result<AgencyAuditApplication> getApplicationById(@PathVariable Long id, HttpSession session) {
        // 1. 登录状态验证（Controller层处理请求相关的验证）
        Object loginAdmin = session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return Result.error("未登录，请先登录");
        }

        // 2. 调用Service层处理核心业务逻辑
        AgencyAuditApplication application = applicationService.getApplicationById(id);

        // 3. 统一返回响应结果
        if (application == null) {
            return Result.error("申请表单不存在");
        }
        return Result.success(application);
    }
}
