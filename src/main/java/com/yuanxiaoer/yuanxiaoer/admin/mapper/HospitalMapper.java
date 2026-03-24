package com.yuanxiaoer.yuanxiaoer.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalSelectVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医院 Mapper 接口
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {

    /**
     * 获取所有省份（不去重，基于 agency 关联）
     * @return 省份列表
     */
    List<String> getProvincesWithAgency();

    /**
     * 获取指定省份下的城市（不去重，基于 agency 关联）
     * @param province 省份
     * @return 城市列表
     */
    List<String> getCitiesByProvince(@Param("province") String province);

    /**
     * 获取指定城市下的医院（不去重，基于 agency 关联）
     * @param city 城市
     * @return 医院列表
     */
    List<Hospital> getHospitalsByCity(@Param("city") String city);
    
    /**
     * 获取所有有医院的省份（全新方法）
     * @return 省份列表
     */
    List<String> getAllProvinces();
    
    /**
     * 根据省份获取所有城市（全新方法）
     * @param province 省份
     * @return 城市列表
     */
    List<String> getAllCitiesByProvince(@Param("province") String province);
    
    /**
     * 根据城市获取所有医院（全新方法，返回 VO）
     * @param city 城市
     * @return 医院列表（包含 id 和名称）
     */
    List<HospitalSelectVO> getAllHospitalsByCity(@Param("city") String city);
    
    /**
     * 根据医院 ID 列表查询医院信息
     * @param hospitalIds 医院 ID 列表
     * @return 医院信息列表
     */
    List<HospitalInfoVO> getHospitalsByIds(@Param("hospitalIds") List<Long> hospitalIds);
    
    /**
     * 根据医院名称模糊查询医院信息
     * @param keyword 搜索关键词
     * @return 医院信息列表
     */
    List<Hospital> searchHospitalsByName(@Param("keyword") String keyword);
    
    /**
     * 根据医院名称模糊查询医院信息（分页）
     * @param page 分页参数
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    IPage<Hospital> searchHospitalsByName(Page<Hospital> page, @Param("keyword") String keyword);
    
    /**
     * 根据城市和医院等级查询所有医院（分页）
     * @param page 分页参数
     * @param city 城市名称
     * @param hospitalLevel 医院等级（可选，为空则查询所有等级）
     * @return 分页结果
     */
    IPage<Hospital> getHospitalsByCityAndLevel(Page<Hospital> page, 
                                                @Param("city") String city, 
                                                @Param("hospitalLevel") String hospitalLevel);
}
