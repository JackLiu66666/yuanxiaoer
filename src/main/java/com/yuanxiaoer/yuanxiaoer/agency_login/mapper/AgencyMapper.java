package com.yuanxiaoer.yuanxiaoer.agency_login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 代理商Mapper
 */
@Mapper
public interface AgencyMapper extends BaseMapper<Agency> {
    /**
     * 根据手机号/用户名查询代理商
     */
    Agency selectByLoginKey(@Param("loginKey") String loginKey);
    /**
     * 修改审核状态
     */
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus);
}
