package com.yuanxiaoer.yuanxiaoer.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;

import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper
 */
@Mapper
public interface SysAdminMapper extends BaseMapper<SysAdmin> {
    // 继承BaseMapper后，无需手动写CRUD方法，MyBatisPlus自动实现
}
