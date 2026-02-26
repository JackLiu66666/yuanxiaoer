package com.yuanxiaoer.yuanxiaoer.ic_login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 保险公司Mapper
 */
@Mapper
public interface InsuranceCompanyMapper extends BaseMapper<InsuranceCompany> {
    /**
     * 根据手机号/ID/用户名查询保险公司
     */
    InsuranceCompany selectByLoginKey(@Param("loginKey") String loginKey);
    /**
     * 修改审核状态
     */
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus);
}
