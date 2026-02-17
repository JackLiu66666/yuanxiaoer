package com.yuanxiaoer.yuanxiaoer.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.common.Result;
import com.yuanxiaoer.yuanxiaoer.admin.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.SysAdminMapper;
import org.springframework.stereotype.Service;

/**
 * 管理员Service
 */
@Service
public class SysAdminService extends ServiceImpl<SysAdminMapper, SysAdmin> {

    /**
     * 管理员登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
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
}
