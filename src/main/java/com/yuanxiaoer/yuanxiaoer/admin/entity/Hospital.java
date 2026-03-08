package com.yuanxiaoer.yuanxiaoer.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 医院实体类
 */
@Data
@TableName("hospital")
public class Hospital {
    @TableId(type = IdType.AUTO)
    private Long id; // 唯一 ID
    private String province; // 省份
    private String city; // 城市
    private String districtCounty; // 区/县
    private String hospitalName; // 医院名称
    private String hospitalLevel; // 医院等级
}
