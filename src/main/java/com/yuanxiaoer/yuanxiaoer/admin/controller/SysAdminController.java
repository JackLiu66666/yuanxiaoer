package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanxiaoer.yuanxiaoer.admin.dto.AdminLoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.service.SysAdminService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyApplyVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.service.AgencyAuditApplicationService;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员控制器
 */
@Tag(name = "系统管理员管理", description = "管理员登录、登出及审核管理接口")
@RestController
@RequestMapping("/admin")
public class SysAdminController {

    @Resource
    private SysAdminService sysAdminService;

    @Resource
    private AgencyAuditApplicationService applicationService;

    /**
     * 登录接口
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Operation(summary = "管理员登录", description = "管理员通过账号密码登录系统")
    @PostMapping("/login")
    public Result<SysAdmin> login(@RequestBody AdminLoginDTO loginDTO, HttpSession session) {
        Result<SysAdmin> result = sysAdminService.login(loginDTO);
        if (result.getCode() == 200) {
            session.setAttribute("loginAdmin", result.getData());
        }
        return result;
    }
    /**
     * 登出接口
     * @return 登出结果
    */
    @Operation(summary = "管理员登出", description = "退出当前登录状态")
    @PostMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.removeAttribute("loginAdmin"); // 移除登录态
        session.invalidate(); // 销毁 Session
        return Result.success("退出登录成功");
    }

    /**
     * 分页查询代理商申请列表
     * @param current 当前页（默认 1）
     * @param size 每页条数（默认 20）
     */
    @Operation(summary = "分页查询代理商申请列表", description = "查询待审核的代理商申请记录")
    @GetMapping("/agencyApplicationReview/list")
    public Result<IPage<AgencyApplyVO>> getApplyList(
            @Parameter(description = "当前页码", example = "1") @RequestParam(defaultValue = "1") Long current,
            @Parameter(description = "每页条数", example = "20") @RequestParam(defaultValue = "20") Long size,
            HttpSession session) {
        // 1. 登录状态验证（Controller层处理请求相关的验证）
        Object loginAdmin = session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return Result.error("未登录，请先登录");
        }
        // 2. 调用Service层处理核心业务逻辑
        Page<AgencyApplyVO> page = new Page<>(current, size);
        IPage<AgencyApplyVO> applyList = sysAdminService.getApplyList(page);
        return Result.success(applyList);
    }

    /**
     * 根据 ID 查询申请表单详情
     * @param id 申请表 ID
     * @param session 会话（验证登录）
     * @return 表单数据
     */
    @Operation(summary = "查询代理商申请详情", description = "根据申请 ID 查看详细的申请表单信息")
    @GetMapping("/agencyApplicationReview/list/{id}")
    public Result<AgencyAuditApplication> getApplicationById(
            @Parameter(description = "申请表 ID", example = "1") @PathVariable Long id, 
            HttpSession session) {
        // 1. 登录状态验证（Controller 层处理请求相关的验证）
        Object loginAdmin = session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return Result.error("未登录，请先登录");
        }

        // 2. 调用 Service 层处理核心业务逻辑
        AgencyAuditApplication application = applicationService.getApplicationById(id);

        // 3. 统一返回响应结果
        if (application == null) {
            return Result.error("申请表单不存在");
        }
        return Result.success(application);
    }

    /**
     * 审核通过
     * @param id 申请 ID
     * @param session 会话（验证登录）
     * @return 审核结果
     */
    @Operation(summary = "审核通过代理商申请", description = "将待审核的代理商申请标记为审核通过")
    @PostMapping("/agencyApplicationReview/approve/{id}")
    public Result<String> approveApplication(
            @Parameter(description = "申请 ID", example = "1") @PathVariable Long id, 
            HttpSession session) {
        // 1. 登录状态验证
        Object loginAdmin = session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return Result.error("未登录，请先登录");
        }

        // 2. 调用 Service 层处理审核逻辑
        return sysAdminService.approveApplication(id);
    }

    /**
     * 审核不通过
     * @param id 申请 ID
     * @param session 会话（验证登录）
     * @return 审核结果
     */
    @Operation(summary = "审核不通过代理商申请", description = "将待审核的代理商申请标记为审核不通过")
    @PostMapping("/agencyApplicationReview/reject/{id}")
    public Result<String> rejectApplication(
            @Parameter(description = "申请 ID", example = "1") @PathVariable Long id, 
            HttpSession session) {
        // 1. 登录状态验证
        Object loginAdmin = session.getAttribute("loginAdmin");
        if (loginAdmin == null) {
            return Result.error("未登录，请先登录");
        }

        // 2. 调用 Service 层处理审核逻辑
        return sysAdminService.rejectApplication(id);
    }
}
