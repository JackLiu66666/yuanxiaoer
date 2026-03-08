package com.yuanxiaoer.yuanxiaoer.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 医院与代理商关系实体类
 */
@Data
@TableName("hospital_agency_relation")
public class HospitalAgencyRelation {
    @TableId(type = IdType.AUTO)
    private Long id; // 唯一 ID
    private Long hospitalId; // 医院 ID
    private Long agencyId; // 代理商 ID
}
