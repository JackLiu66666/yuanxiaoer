package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.SysAdminMapper;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyApplyVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper.AgencyAuditApplicationMapper;
import com.yuanxiaoer.yuanxiaoer.common.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * 管理员服务实现类
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    /**
     * 管理员登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public Result<SysAdmin> login(LoginDTO loginDTO) {
        // 1. 校验参数
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return Result.error("账号不能为空");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }

        // 2. 根据账号查询管理员
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAdmin::getUsername, loginDTO.getUsername());
        SysAdmin sysAdmin = this.getOne(queryWrapper);

        // 3. 校验账号是否存在
        if (sysAdmin == null) {
            return Result.error("账号不存在");
        }

        // 4. 校验密码（实际项目要加密后对比，这里先明文）
        if (!sysAdmin.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        // 5. 登录成功（实际项目这里要生成token，这里简化）
        return Result.success(sysAdmin);
    }

    /**
     * 获取申请列表
     * @param page 分页参数
     * @return 申请列表
     */
    @Autowired
    private AgencyAuditApplicationMapper agencyAuditApplicationMapper;

    @Override
    public IPage<AgencyApplyVO> getApplyList(Page<AgencyApplyVO> page) {
        return agencyAuditApplicationMapper.selectApplyPage(page);
    }
}
