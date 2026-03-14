package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyBusinessScopeRelationService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.BusinessScopeAgencyVO;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务范围管理控制器
 */
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
    @GetMapping("/{businessScope}")
    public Result<List<BusinessScopeAgencyVO>> getAgenciesByBusinessScope(@PathVariable Integer businessScope) {
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
