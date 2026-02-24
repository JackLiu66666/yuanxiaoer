package com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgencyAuditApplicationMapper extends BaseMapper<AgencyAuditApplication> {
    // 检查代理商是否已提交申请
    Integer countByAgencyId(Long agencyId);
}