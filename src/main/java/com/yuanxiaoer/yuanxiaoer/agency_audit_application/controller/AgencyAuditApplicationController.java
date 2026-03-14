package com.yuanxiaoer.yuanxiaoer.agency_audit_application.controller;

import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.service.AgencyAuditApplicationService;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.CityVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalSelectVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.ProvinceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.common.util.BusinessScopeUtil;
import com.yuanxiaoer.yuanxiaoer.common.util.CooperationAreaItem;
import com.yuanxiaoer.yuanxiaoer.common.util.CooperationAreaUtil;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agencyAuditApplication")
public class AgencyAuditApplicationController {

    @Autowired
    private AgencyAuditApplicationService applicationService;
    
    @Autowired
    private AgencyAuditApplicationService agencyAuditApplicationService;

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
            Long agencyId = ((Agency) session.getAttribute("loginAgency")).getId();
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
    /**
     * 获取代理商审核状态
     */
    @GetMapping("/audit-status")
    public ResponseEntity<Map<String, Object>> getAuditStatus(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 从 session 获取代理商对象
            Agency loginAgency = (Agency) session.getAttribute("loginAgency");
            if (loginAgency == null) {
                // 未登录或 session 失效，返回 401
                result.put("code", 401);
                result.put("msg", "未登录或会话已过期");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            Integer auditStatus = loginAgency.getAuditStatus();
            result.put("code", 200);
            result.put("data", auditStatus);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取审核状态失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取所有省份列表
     * @return 省份列表
     */
    @GetMapping("/provinces")
    public ResponseEntity<Map<String, Object>> getProvinces() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<ProvinceVO> provinces = agencyAuditApplicationService.getProvinces();
            result.put("code", 200);
            result.put("data", provinces);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取省份失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 根据省份获取城市列表
     * @param province 省份名称
     * @return 城市列表
     */
    @GetMapping("/cities")
    public ResponseEntity<Map<String, Object>> getCitiesByProvince(@RequestParam("province") String province) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (province == null || province.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "省份参数不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            List<CityVO> cities = agencyAuditApplicationService.getCitiesByProvince(province);
            result.put("code", 200);
            result.put("data", cities);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取城市失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 根据城市获取医院列表
     * @param city 城市名称
     * @return 医院列表（包含 id 和名称）
     */
    @GetMapping("/hospitals")
    public ResponseEntity<Map<String, Object>> getHospitalsByCity(@RequestParam("city") String city) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (city == null || city.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "城市参数不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            
            List<HospitalSelectVO> hospitals = agencyAuditApplicationService.getHospitalsByCity(city);
            result.put("code", 200);
            result.put("data", hospitals);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "获取医院失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 验证业务范围数据
     * @param codes 业务代码列表，如 [1,2,3]
     * @return 验证结果
     */
    @PostMapping("/validate-business-scope")
    public ResponseEntity<Map<String, Object>> validateBusinessScope(@RequestBody List<Integer> codes) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean isValid = agencyAuditApplicationService.validateBusinessScope(codes);
            result.put("code", 200);
            result.put("data", isValid);
            
            if (isValid) {
                // 返回业务名称列表
                List<String> names = BusinessScopeUtil.getNames(codes);
                result.put("names", names);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "验证失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
    
    /**
     * 验证合作区域数据
     * @param items 合作区域列表
     * @return 验证结果
     */
    @PostMapping("/validate-cooperation-area")
    public ResponseEntity<Map<String, Object>> validateCooperationArea(@RequestBody List<CooperationAreaItem> items) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean isValid = agencyAuditApplicationService.validateCooperationArea(items);
            result.put("code", 200);
            result.put("data", isValid);
            
            if (isValid) {
                // 返回格式化后的显示文本
                List<String> displayTexts = items.stream()
                    .map(CooperationAreaItem::getDisplayText)
                    .collect(java.util.stream.Collectors.toList());
                result.put("displayTexts", displayTexts);
                
                // 返回总医院数量
                int totalHospitals = CooperationAreaUtil.getTotalHospitalCount(items);
                result.put("totalHospitals", totalHospitals);
            }
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "验证失败：" + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }
}
