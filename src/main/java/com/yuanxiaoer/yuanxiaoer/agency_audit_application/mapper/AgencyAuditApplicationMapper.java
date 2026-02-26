package com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyApplyVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Mapper
public interface AgencyAuditApplicationMapper extends BaseMapper<AgencyAuditApplication> {
    /**
     * 检查代理商是否已提交申请
     */
    Integer countByAgencyId(Long agencyId);
    /**
     * 分页查询代理商申请列表（关联agency表获取审核状态）
     */
    IPage<AgencyApplyVO> selectApplyPage(Page<AgencyApplyVO> page);
}