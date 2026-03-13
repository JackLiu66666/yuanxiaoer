package com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo;

import lombok.Data;

/**
 * 医院信息 VO（用于返回完整的医院信息）
 */
@Data
public class HospitalInfoVO {
    /** 医院 ID */
    private Long id;
    
    /** 省份 */
    private String province;
    
    /** 城市 */
    private String city;
    
    /** 区县 */
    private String districtCounty;
    
    /** 医院名称 */
    private String hospitalName;
    
    /** 医院等级 */
    private String hospitalLevel;
}
