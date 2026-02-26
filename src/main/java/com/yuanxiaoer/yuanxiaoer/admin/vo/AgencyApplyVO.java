package com.yuanxiaoer.yuanxiaoer.admin.vo;

import lombok.Data;
/**
 * 代理商申请列表返回VO
 */
@Data
public class AgencyApplyVO {
    /** 申请ID */
    private Long id;
    /** 代理商全称 */
    private String agencyFullName;
    /** 审核状态：0-未审核 1-待审核 2-审核通过 3-审核不通过 */
    private Integer auditStatus;
    /** 审核状态文本（前端也可处理，后端冗余返回提升体验） */
    private String auditStatusText;
}
