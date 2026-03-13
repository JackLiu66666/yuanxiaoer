package com.yuanxiaoer.yuanxiaoer.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 代理商业务范围关系实体类
 */
@Data
@TableName("agency_business_scope_relation")
public class AgencyBusinessScopeRelation {
    @TableId(type = IdType.AUTO)
    private Long id; // 唯一 ID
    private Long agencyId; // 代理商 ID
    private Integer businessScope; // 业务范围代码（1-6）
}
