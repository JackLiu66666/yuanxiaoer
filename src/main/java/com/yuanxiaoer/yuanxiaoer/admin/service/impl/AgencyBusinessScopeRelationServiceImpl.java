package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyBusinessScopeRelation;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.AgencyBusinessScopeRelationMapper;
import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyBusinessScopeRelationService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.BusinessScopeAgencyVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理商业务范围关系服务实现类
 */
@Service
public class AgencyBusinessScopeRelationServiceImpl extends ServiceImpl<AgencyBusinessScopeRelationMapper, AgencyBusinessScopeRelation> implements AgencyBusinessScopeRelationService {

    @Override
    public List<BusinessScopeAgencyVO> getAgenciesByBusinessScope(Integer businessScope) {
        // 校验业务范围代码
        if (businessScope == null || businessScope < 1 || businessScope > 6) {
            throw new IllegalArgumentException("业务范围代码必须在 1-6 之间");
        }
        
        return this.baseMapper.getAgenciesByBusinessScope(businessScope);
    }
}
