package com.yuanxiaoer.yuanxiaoer.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医院 Mapper 接口
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {

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
    List<String> getCitiesByProvince(@Param("province") String province);

    /**
     * 查询指定城市下有 agency 的医院
     * @param city 城市
     * @return 医院列表
     */
    List<Hospital> getHospitalsByCity(@Param("city") String city);
}
