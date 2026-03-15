package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.yuanxiaoer.yuanxiaoer.admin.dto.AdminLoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyApplyVO;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * 管理员服务接口
 */
public interface SysAdminService extends IService<SysAdmin> {

    /**
     * 管理员登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    Result<SysAdmin> login(AdminLoginDTO loginDTO);

    /**
     * 分页查询代理商申请列表
     */
    IPage<AgencyApplyVO> getApplyList(Page<AgencyApplyVO> page);

    /**
     * 审核通过
     * @param id 申请 ID
     * @return 审核结果
     */
    Result<String> approveApplication(Long id);

    /**
     * 审核不通过
     * @param id 申请 ID
     * @return 审核结果
     */
    Result<String> rejectApplication(Long id);

}
