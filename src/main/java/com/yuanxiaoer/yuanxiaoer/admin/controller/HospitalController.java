package com.yuanxiaoer.yuanxiaoer.admin.controller;

import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;
import com.yuanxiaoer.yuanxiaoer.admin.service.HospitalService;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 医院管理控制器
 */
@RestController
@RequestMapping("/admin/hospital")
public class HospitalController {

    @Resource
    private HospitalService hospitalService;

    /**
     * 查询所有有 agency 的省份
     * @return 省份列表
     */
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
    @GetMapping("/cities/{province}")
    public Result<List<String>> getCitiesByProvince(@PathVariable String province) {
        List<String> cities = hospitalService.getCitiesByProvince(province);
        return Result.success(cities);
    }

    /**
     * 查询指定城市下有 agency 的医院
     * @param city 城市
     * @return 医院列表
     */
    @GetMapping("/hospitals/{city}")
    public Result<List<Hospital>> getHospitalsByCity(@PathVariable String city) {
        List<Hospital> hospitals = hospitalService.getHospitalsByCity(city);
        return Result.success(hospitals);
    }
}
