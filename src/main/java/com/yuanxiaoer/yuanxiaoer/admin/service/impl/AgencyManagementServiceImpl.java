package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.agency_login.mapper.AgencyMapper;
import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyManagementService;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyBaseVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 代理商管理服务实现类
 */
@Service
public class AgencyManagementServiceImpl extends ServiceImpl<AgencyMapper, Agency> implements AgencyManagementService {

    @Override
    public IPage<AgencyBaseVO> getAllAgencies(Page<AgencyBaseVO> page) {
        // 1. 查询所有代理商（不分页）
        List<Agency> agencies = this.list();
        
        // 2. 手动分页并转换为 VO 对象
        IPage<AgencyBaseVO> resultPage = new Page<>(page.getCurrent(), page.getSize(), getTotal(agencies));
        resultPage.setRecords(getSubList(agencies, (int)page.getCurrent(), (int)page.getSize()).stream()
            .map(this::convertToVO)
            .collect(Collectors.toList()));
        
        return resultPage;
    }

    @Override
    public IPage<AgencyBaseVO> getAgenciesByPhone(String phone, Page<AgencyBaseVO> page) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Agency> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(phone)) {
            queryWrapper.like(Agency::getPhone, phone);
        }
        
        // 2. 执行分页查询（使用 Agency 类型的 Page）
        Page<Agency> agencyPage = new Page<>(page.getCurrent(), page.getSize());
        IPage<Agency> result = this.page(agencyPage, queryWrapper);
        
        // 3. 转换为 VO 对象
        IPage<AgencyBaseVO> resultPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        resultPage.setRecords(result.getRecords().stream()
            .map(this::convertToVO)
            .collect(Collectors.toList()));
        
        return resultPage;
    }

    /**
     * 将 Agency 实体转换为 AgencyBaseVO
     * @param agency 代理商实体
     * @return VO 对象
     */
    private AgencyBaseVO convertToVO(Agency agency) {
        AgencyBaseVO vo = new AgencyBaseVO();
        vo.setId(agency.getId());
        vo.setUsername(agency.getUsername());
        vo.setPhone(agency.getPhone());
        vo.setAuditStatus(agency.getAuditStatus());
        
        // 设置审核状态文本
        String statusText;
        switch (agency.getAuditStatus()) {
            case 0:
                statusText = "未审核";
                break;
            case 1:
                statusText = "待审核";
                break;
            case 2:
                statusText = "审核通过";
                break;
            case 3:
                statusText = "审核不通过";
                break;
            default:
                statusText = "未知状态";
        }
        vo.setAuditStatusText(statusText);
        
        return vo;
    }
    
    /**
     * 获取列表总数
     * @param list 数据列表
     * @return 总数
     */
    private long getTotal(List<Agency> list) {
        return list != null ? list.size() : 0;
    }
    
    /**
     * 分页获取子列表
     * @param list 数据列表
     * @param pageNum 页码（从 1 开始）
     * @param pageSize 每页条数
     * @return 分页后的子列表
     */
    private List<Agency> getSubList(List<Agency> list, int pageNum, int pageSize) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        
        int total = list.size();
        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, total);
        
        if (fromIndex >= total) {
            return java.util.Collections.emptyList();
        }
        
        return list.subList(fromIndex, toIndex);
    }
}
