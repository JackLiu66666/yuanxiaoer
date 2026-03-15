package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyBusinessScopeRelationService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.BusinessScopeAgencyVO;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务范围管理控制器
 */
@Tag(name = "业务范围管理", description = "按业务范围代码查询代理商列表")
@RestController
@RequestMapping("/admin/businessScope")
public class BusinessScopeController {

    @Resource
    private AgencyBusinessScopeRelationService agencyBusinessScopeRelationService;

    /**
     * 根据业务范围代码查询代理商列表
     * @param businessScope 业务范围代码（1-6）
     *        1: 院陪诊
     *        2: 院陪护
     *        3: 院办办
     *        4: 院保保
     *        5: 院垫垫
     *        6: 院探探
     * @return 代理商列表
     */
    @Operation(summary = "按业务范围查询代理商", description = "根据业务范围代码（1-6）查询对应的代理商列表")
    @GetMapping("/{businessScope}")
    public Result<List<BusinessScopeAgencyVO>> getAgenciesByBusinessScope(
            @Parameter(description = "业务范围代码（1-6）\n1: 院陪诊\n2: 院陪护\n3: 院办办\n4: 院保保\n5: 院垫垫\n6: 院探探", 
                      example = "1", 
                      required = true) 
            @PathVariable Integer businessScope) {
        try {
            List<BusinessScopeAgencyVO> agencies = agencyBusinessScopeRelationService.getAgenciesByBusinessScope(businessScope);
            return Result.success(agencies);
        } catch (IllegalArgumentException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
