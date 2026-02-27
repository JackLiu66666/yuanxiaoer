package com.yuanxiaoer.yuanxiaoer.nurse_login.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.nurse_login.entity.Nurse;
import com.yuanxiaoer.yuanxiaoer.nurse_login.mapper.NurseMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 护士Service实现类
 */
@Service
public class NurseServiceImpl extends ServiceImpl<NurseMapper, Nurse> implements NurseService {

    // 密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result<?> register(Nurse nurse) {
        // 1. 校验用户名是否重复
        LambdaQueryWrapper<Nurse> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(Nurse::getUsername, nurse.getUsername());
        if (this.count(usernameWrapper) > 0) {
            return Result.error("用户名已存在");
        }

        // 2. 校验手机号是否重复
        LambdaQueryWrapper<Nurse> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(Nurse::getPhone, nurse.getPhone());
        if (this.count(phoneWrapper) > 0) {
            return Result.error("手机号已存在");
        }

        // 3. 密码加密
        nurse.setPassword(passwordEncoder.encode(nurse.getPassword()));
        // 4. 默认审核状态：0-未审核
        nurse.setAuditStatus(0);
        // 5. 默认在线状态：0-离线
        nurse.setOnlineStatus(0);

        // 6. 保存数据
        boolean save = this.save(nurse);
        if (save) {
            return Result.success("注册成功，请等待审核");
        } else {
            return Result.error("注册失败");
        }
    }

    @Override
    public Result<?> login(String loginKey, String password) {
        // 1. 根据手机号/ID/用户名查询护士
        Nurse nurse = this.baseMapper.selectByLoginKey(loginKey);
        if (nurse == null) {
            return Result.error("用户不存在");
        }

        // 2. 校验密码
        if (!passwordEncoder.matches(password, nurse.getPassword())) {
            return Result.error("密码错误");
        }

        // 3. 移除密码，返回用户信息（供存入Session）
        nurse.setPassword(null); // 敏感信息置空
        return Result.success(nurse); // 返回nurse对象
    }

    @Override
    public Result<?> setOnlineStatus(Long id, Integer onlineStatus) {
        int result = this.baseMapper.updateOnlineStatus(id, onlineStatus);
        if (result > 0) {
            return Result.success("状态更新成功");
        } else {
            return Result.error("状态更新失败");
        }
    }
}