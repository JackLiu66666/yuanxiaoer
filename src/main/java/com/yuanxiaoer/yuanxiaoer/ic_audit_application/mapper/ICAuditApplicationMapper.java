package com.yuanxiaoer.yuanxiaoer.ic_audit_application.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yuanxiaoer.yuanxiaoer.admin.vo.ICApplyVO;
import com.yuanxiaoer.yuanxiaoer.ic_audit_application.entity.ICAuditApplication;

@Mapper
public interface ICAuditApplicationMapper extends BaseMapper<ICAuditApplication> {
    
    /**
     * 检查保险公司是否已提交申请
     */
    Integer countByInsuranceCompanyId(Long insuranceCompanyId);
    /**
     * 分页查询保险公司申请列表（关联insurance_company表获取审核状态）
     */
    IPage<ICApplyVO> selectApplyPage(Page<ICApplyVO> page);

}
