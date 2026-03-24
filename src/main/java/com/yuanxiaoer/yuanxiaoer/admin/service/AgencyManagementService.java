package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanxiaoer.yuanxiaoer.agency_login.entity.Agency;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyBaseVO;

/**
 * 代理商管理服务接口
 */
public interface AgencyManagementService extends IService<Agency> {
    
    /**
     * 分页查询所有代理商的基础信息
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<AgencyBaseVO> getAllAgencies(Page<AgencyBaseVO> page);
    
    /**
     * 根据手机号分页查询代理商的基础信息
     * @param phone 手机号
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<AgencyBaseVO> getAgenciesByPhone(String phone, Page<AgencyBaseVO> page);
}
