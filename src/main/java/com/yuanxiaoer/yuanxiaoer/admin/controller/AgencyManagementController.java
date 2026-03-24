package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyManagementService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyBaseVO;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代理商管理控制器
 */
@Tag(name = "代理商管理", description = "管理员查看代理商相关信息")
@RestController
@RequestMapping("/admin/agencyManagement")
public class AgencyManagementController {

    @Resource
    private AgencyManagementService agencyManagementService;

    /**
     * 分页查询所有代理商的基础信息
     * @param pageNum 页码（默认 1）
     * @param pageSize 每页条数（默认 20）
     * @return 分页结果
     */
    @Operation(summary = "分页查询所有代理商", description = "获取系统中所有注册代理商的基础信息（不包含敏感字段），支持分页查询")
    @GetMapping("/list")
    public Result<IPage<AgencyBaseVO>> getAllAgencies(
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数", example = "20") 
            @RequestParam(defaultValue = "20") Long pageSize) {
        try {
            Page<AgencyBaseVO> page = new Page<>(pageNum, pageSize);
            IPage<AgencyBaseVO> resultPage = agencyManagementService.getAllAgencies(page);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据手机号分页查询代理商的基础信息
     * @param phone 手机号（支持模糊查询）
     * @param pageNum 页码（默认 1）
     * @param pageSize 每页条数（默认 20）
     * @return 分页结果
     */
    @Operation(summary = "根据手机号查询代理商", description = "通过代理商手机号进行模糊搜索，返回匹配的代理商列表，支持分页查询")
    @GetMapping("/phone")
    public Result<IPage<AgencyBaseVO>> getAgenciesByPhone(
            @Parameter(description = "手机号（支持模糊匹配）", example = "138") 
            @RequestParam(required = false) String phone,
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数", example = "20") 
            @RequestParam(defaultValue = "20") Long pageSize) {
        try {
            Page<AgencyBaseVO> page = new Page<>(pageNum, pageSize);
            IPage<AgencyBaseVO> resultPage = agencyManagementService.getAgenciesByPhone(phone, page);
            return Result.success(resultPage);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
}
