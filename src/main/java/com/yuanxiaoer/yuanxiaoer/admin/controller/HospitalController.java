package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;
import com.yuanxiaoer.yuanxiaoer.admin.service.HospitalService;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 医院管理控制器
 */
@Tag(name = "医院管理", description = "查询有代理商合作的省份、城市及医院信息")
@RestController
@RequestMapping("/admin/hospital")
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    /**
     * 查询所有有 agency 的省份
     * @return 省份列表
     */
    @Operation(summary = "查询合作省份", description = "获取所有有代理商合作的省份列表")
    @GetMapping("/provinces")
    public Result<List<String>> getProvincesWithAgency() {
        List<String> provinces = hospitalService.getProvincesWithAgency();
        return Result.success(provinces);
    }

    /**
     * 查询指定省份下有 agency 的城市
     * @param province 省份
     * @return 城市列表
     */
    @Operation(summary = "查询合作城市", description = "根据省份查询该省下有代理商合作的城市")
    @GetMapping("/cities/{province}")
    public Result<List<String>> getCitiesByProvinceWithAgency(
            @Parameter(description = "省份名称", example = "广东省", required = true) 
            @PathVariable String province) {
        List<String> cities = hospitalService.getCitiesByProvince(province);
        return Result.success(cities);
    }

    /**
     * 查询指定城市下有 agency 的医院
     * @param city 城市
     * @return 医院列表
     */
    @Operation(summary = "查询合作医院", description = "根据城市查询该城市下有代理商合作的医院列表")
    @GetMapping("/hospitals/{city}")
    public Result<List<Hospital>> getHospitalsByCity(
            @Parameter(description = "城市名称", example = "广州市", required = true) 
            @PathVariable String city) {
        List<Hospital> hospitals = hospitalService.getHospitalsByCity(city);
        return Result.success(hospitals);
    }

    /**
     * 根据医院名称模糊搜索医院信息
     * @param keyword 搜索关键词
     * @param pageNum 页码（默认 1）
     * @param pageSize 每页条数（固定 20）
     * @return 分页结果
     */
    @Operation(summary = "模糊搜索医院", description = "根据输入的关键词对医院名称进行模糊匹配，返回所有相关医院信息")
    @GetMapping("/search")
    public Result<IPage<Hospital>> searchHospitals(
            @Parameter(description = "搜索关键词（医院名称的一部分）", example = "人民", required = true)
            @RequestParam String keyword,
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数（固定 20）", example = "20") 
            @RequestParam(defaultValue = "20") Long pageSize) {
        Page<Hospital> page = new Page<>(pageNum, pageSize);
        IPage<Hospital> hospitalPage = hospitalService.searchHospitalsByName(keyword, page);
        return Result.success(hospitalPage);
    }

    /**
     * 根据省份查询所有城市
     * @param province 省份名称
     * @return 城市列表
     */
    @Operation(summary = "查询省份下的城市", description = "根据省份名称查询该省下的所有城市列表")
    @GetMapping("/cities")
    public Result<List<String>> getCitiesByProvince(
            @Parameter(description = "省份名称", example = "广东省", required = true)
            @RequestParam String province) {
        List<String> cities = hospitalService.getAllCitiesByProvince(province);
        return Result.success(cities);
    }

    /**
     * 根据城市和医院等级查询所有医院
     * @param city 城市名称（传入"全省"则查询该省下所有医院）
     * @param hospitalLevel 医院等级（可选，不传则查询所有等级）
     * @param pageNum 页码（默认 1）
     * @param pageSize 每页条数（固定 20）
     * @return 分页结果
     */
    @Operation(summary = "查询城市下的医院", description = "根据城市名称和医院等级查询医院列表，支持全省查询")
    @GetMapping("/hospitals")
    public Result<IPage<Hospital>> getHospitalsByCityAndLevel(
            @Parameter(description = "城市名称（传入'全省'则查询该省所有医院）", example = "广州市", required = true)
            @RequestParam String city,
            @Parameter(description = "医院等级（可选，如'三甲'、'二甲'等，不传则查询所有等级）", example = "三甲")
            @RequestParam(required = false) String hospitalLevel,
            @Parameter(description = "当前页码", example = "1") 
            @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页条数（固定 20）", example = "20") 
            @RequestParam(defaultValue = "20") Long pageSize) {
        Page<Hospital> page = new Page<>(pageNum, pageSize);
        IPage<Hospital> hospitalPage = hospitalService.getHospitalsByCityAndLevel(city, hospitalLevel, page);
        return Result.success(hospitalPage);
    }
}
