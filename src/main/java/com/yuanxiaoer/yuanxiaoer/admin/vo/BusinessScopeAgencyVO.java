package com.yuanxiaoer.yuanxiaoer.admin.vo;

import lombok.Data;

/**
 * 业务范围代理商查询 VO
 */
@Data
public class BusinessScopeAgencyVO {
    /** 代理商 ID */
    private Long agencyId;
    /** 机构全称 */
    private String agencyFullName;
}
