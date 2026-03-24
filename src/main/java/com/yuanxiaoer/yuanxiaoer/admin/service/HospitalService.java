package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;

import java.util.List;

/**
 * 医院服务接口
 */
public interface HospitalService extends IService<Hospital> {

    /**
     * 查询所有有 agency 的省份
     * @return 省份列表
     */
    List<String> getProvincesWithAgency();

    /**
     * 查询指定省份下有 agency 的城市
     * @param province 省份
     * @return 城市列表
     */
    List<String> getCitiesByProvince(String province);

    /**
     * 查询指定城市下有 agency 的医院
     * @param city 城市
     * @return 医院列表
     */
    List<Hospital> getHospitalsByCity(String city);
    
    /**
     * 根据医院名称模糊搜索医院信息
     * @param keyword 搜索关键词
     * @return 医院信息列表
     */
    List<Hospital> searchHospitalsByName(String keyword);
    
    /**
     * 根据医院名称模糊搜索医院信息（分页）
     * @param keyword 搜索关键词
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<Hospital> searchHospitalsByName(String keyword, Page<Hospital> page);
    
    /**
     * 根据省份查询所有城市（不分页）
     * @param province 省份名称
     * @return 城市列表
     */
    List<String> getAllCitiesByProvince(String province);
    
    /**
     * 根据城市和医院等级查询所有医院（分页）
     * @param city 城市名称
     * @param hospitalLevel 医院等级（可选，为空则查询所有等级）
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<Hospital> getHospitalsByCityAndLevel(String city, String hospitalLevel, Page<Hospital> page);
}
