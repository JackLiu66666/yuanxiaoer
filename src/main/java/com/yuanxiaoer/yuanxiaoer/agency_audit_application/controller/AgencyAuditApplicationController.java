package com.yuanxiaoer.yuanxiaoer.agency_audit_application.controller;

import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.service.AgencyAuditApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/agencyAuditApplication")
public class AgencyAuditApplicationController {

    @Autowired
    private AgencyAuditApplicationService applicationService;

    /**
     * 提交代理商审核申请
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitApplication(
            AgencyAuditApplication application,
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
     * 检查当前代理商是否已提交申请
     */
    @GetMapping("/check-applied")
    public ResponseEntity<Map<String, Object>> checkApplied(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从session获取代理商ID
            Long agencyId = ((com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency) session.getAttribute("loginAgency")).getId();
            boolean hasApplied = applicationService.hasApplied(agencyId);
            result.put("code", 200);
            result.put("data", hasApplied);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "检查失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
