package com.yuanxiaoer.yuanxiaoer.ic_audit_application.controller;

import com.yuanxiaoer.yuanxiaoer.ic_audit_application.entity.ICAuditApplication;
import com.yuanxiaoer.yuanxiaoer.ic_audit_application.service.ICAuditApplicationService;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/icAuditApplication")
public class ICAuditApplicationController {

    @Autowired
    private ICAuditApplicationService applicationService;

        /**
     * 提交保险公司审核申请
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitApplication(
            ICAuditApplication application,
            @RequestParam("sealFile") MultipartFile sealFile,
            HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            String msg = applicationService.submitApplication(application, sealFile, session);
            if (msg.contains("成功")) {
                result.put("code", 200);
                result.put("msg", msg);
                return ResponseEntity.ok(result);
            } else {
                result.put("code", 400);
                result.put("msg", msg);
                return ResponseEntity.badRequest().body(result);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "服务器异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 检查当前保险公司是否已提交申请
     */
    @GetMapping("/check-applied")
    public ResponseEntity<Map<String, Object>> checkApplied(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Long icId = ((InsuranceCompany) session.getAttribute("loginIC")).getId();
            boolean hasApplied = applicationService.hasApplied(icId);
            result.put("code", 200);
            result.put("data", hasApplied);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "检查失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
    /**
     * 获取保险公司审核状态
     */
    @GetMapping("/audit-status")
    public ResponseEntity<Map<String, Object>> getAuditStatus(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            InsuranceCompany loginIC = (InsuranceCompany) session.getAttribute("loginIC");
            if (loginIC == null) {
                // 未登录或 session 失效，返回 401
                result.put("code", 401);
                result.put("msg", "未登录或会话已过期");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            Integer auditStatus = loginIC.getAuditStatus();
            result.put("code", 200);
            result.put("data", auditStatus);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取审核状态失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
