package com.yuanxiaoer.yuanxiaoer.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyBusinessScopeRelation;
import com.yuanxiaoer.yuanxiaoer.admin.vo.BusinessScopeAgencyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代理商业务范围关系 Mapper 接口
 */
@Mapper
public interface AgencyBusinessScopeRelationMapper extends BaseMapper<AgencyBusinessScopeRelation> {

    /**
     * 根据业务范围代码查询代理商列表
     * @param businessScope 业务范围代码（1-6）
     * @return 代理商列表（包含 agency_id 和 agency_full_name）
     */
    List<BusinessScopeAgencyVO> getAgenciesByBusinessScope(@Param("businessScope") Integer businessScope);
}
