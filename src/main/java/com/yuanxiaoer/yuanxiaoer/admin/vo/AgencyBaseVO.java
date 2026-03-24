package com.yuanxiaoer.yuanxiaoer.admin.vo;

import lombok.Data;

/**
 * 代理商基础信息 VO（用于管理员查看所有代理商）
 */
@Data
public class AgencyBaseVO {
    /** 代理商 ID */
    private Long id;
    /** 用户名 */
    private String username;
    /** 手机号 */
    private String phone;
    /** 审核状态：0-未审核 1-待审核 2-审核通过 3-审核不通过 */
    private Integer auditStatus;
    /** 审核状态文本 */
    private String auditStatusText;
}
