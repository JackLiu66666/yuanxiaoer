package com.yuanxiaoer.yuanxiaoer.nurse_login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanxiaoer.yuanxiaoer.nurse_login.entity.Nurse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 护士Mapper
 */
@Mapper
public interface NurseMapper extends BaseMapper<Nurse> {
    /**
     * 根据手机号/ID/用户名查询护士
     */
    Nurse selectByLoginKey(@Param("loginKey") String loginKey);
    /**
     * 修改审核状态
     */
    int updateAuditStatus(@Param("id") Long id, @Param("auditStatus") Integer auditStatus);
    /**
     * 修改在线状态
     */
    int updateOnlineStatus(@Param("id") Long id, @Param("onlineStatus") Integer onlineStatus);
}