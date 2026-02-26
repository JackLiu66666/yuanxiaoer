package com.yuanxiaoer.yuanxiaoer.ic_login.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.common.Result;
import com.yuanxiaoer.yuanxiaoer.ic_login.entity.InsuranceCompany;
import com.yuanxiaoer.yuanxiaoer.ic_login.mapper.InsuranceCompanyMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 保险公司Service实现类
 */
@Service
public class InsuranceCompanyServiceImpl extends ServiceImpl<InsuranceCompanyMapper, InsuranceCompany> implements InsuranceCompanyService {

    // 密码加密器
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result<?> register(InsuranceCompany insuranceCompany) {
        // 1. 校验用户名是否重复
        LambdaQueryWrapper<InsuranceCompany> usernameWrapper = new LambdaQueryWrapper<>();
        usernameWrapper.eq(InsuranceCompany::getUsername, insuranceCompany.getUsername());
        if (this.count(usernameWrapper) > 0) {
            return Result.error("用户名已存在");
        }

        // 2. 校验手机号是否重复
        LambdaQueryWrapper<InsuranceCompany> phoneWrapper = new LambdaQueryWrapper<>();
        phoneWrapper.eq(InsuranceCompany::getPhone, insuranceCompany.getPhone());
        if (this.count(phoneWrapper) > 0) {
            return Result.error("手机号已存在");
        }

        // 3. 密码加密
        insuranceCompany.setPassword(passwordEncoder.encode(insuranceCompany.getPassword()));
        // 4. 默认审核状态：0-未审核
        insuranceCompany.setAuditStatus(0);

        // 5. 保存数据
        boolean save = this.save(insuranceCompany);
        if (save) {
            return Result.success("注册成功，请等待审核");
        } else {
            return Result.error("注册失败");
        }
    }

    @Override
    public Result<?> login(String loginKey, String password) {
        // 1. 根据手机号/ID/用户名查询保险公司
        InsuranceCompany insuranceCompany = this.baseMapper.selectByLoginKey(loginKey);
        if (insuranceCompany == null) {
            return Result.error("用户不存在");
        }

        // 2. 校验密码
        if (!passwordEncoder.matches(password, insuranceCompany.getPassword())) {
            return Result.error("密码错误");
        }

        // 4. 登录成功：移除密码，返回用户信息（供存入Session）
        insuranceCompany.setPassword(null); // 敏感信息置空
        return Result.success(insuranceCompany); // 原代码返回"登录成功..."，现在返回agent对象
    }
}
