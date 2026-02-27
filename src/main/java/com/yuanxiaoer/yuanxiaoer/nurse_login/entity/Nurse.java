package com.yuanxiaoer.yuanxiaoer.nurse_login.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 护士实体类
 */
@Data
@TableName("nurse")
public class Nurse {
    @TableId(type = IdType.AUTO)
    private Long id; // 唯一ID
    private String username; // 唯一用户名
    private String password; // 加密密码
    private Integer auditStatus; // 审核状态：0-未审核 1-待审核 2-通过 3-不通过
    private String phone; // 手机号
    private Integer onlineStatus; // 在线状态：0-离线 1-在线
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
}