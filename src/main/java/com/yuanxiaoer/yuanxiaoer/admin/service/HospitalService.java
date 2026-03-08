package com.yuanxiaoer.yuanxiaoer.admin.service;

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
}
