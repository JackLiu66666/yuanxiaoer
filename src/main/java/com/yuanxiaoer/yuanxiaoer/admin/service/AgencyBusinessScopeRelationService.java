package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyBusinessScopeRelation;
import com.yuanxiaoer.yuanxiaoer.admin.vo.BusinessScopeAgencyVO;

import java.util.List;

/**
 * 代理商业务范围关系服务接口
 */
public interface AgencyBusinessScopeRelationService extends IService<AgencyBusinessScopeRelation> {
    
    /**
     * 根据业务范围代码查询代理商列表
     * @param businessScope 业务范围代码（1-6）
     * @return 代理商列表
     */
    List<BusinessScopeAgencyVO> getAgenciesByBusinessScope(Integer businessScope);
}
