package com.yuanxiaoer.yuanxiaoer.agency_login.vo;

import lombok.Data;

/**
 * 代理商信息 VO（用于返回代理商详细信息）
 */
@Data
public class AgencyInfoVO {
    /**
     * 机构全称
     */
    private String agencyFullName;
    
    /**
     * 统一社会信用代码
     */
    private String socialCreditCode;
    
    /**
     * 注册地址
     */
    private String registeredAddress;
    
    /**
     * 机构类型
     */
    private String agencyType;
    
    /**
     * 经营许可证/备案号
     */
    private String businessLicenseNo;
    
    /**
     * 许可证有效期开始
     */
    private java.time.LocalDate licenseValidStart;
    
    /**
     * 许可证有效期结束
     */
    private java.time.LocalDate licenseValidEnd;
    
    /**
     * 法定代表人姓名
     */
    private String legalPersonName;
    
    /**
     * 法定代表人身份证号
     */
    private String legalPersonIdCard;
    
    /**
     * 法定代表人手机号
     */
    private String legalPersonPhone;
    
    /**
     * 对接负责人姓名
     */
    private String contactPersonName;
    
    /**
     * 对接负责人职务
     */
    private String contactPersonPosition;
    
    /**
     * 对接负责人手机号
     */
    private String contactPersonPhone;
    
    /**
     * 对接负责人办公电话
     */
    private String contactPersonOfficePhone;
    
    /**
     * 对接负责人邮箱
     */
    private String contactPersonEmail;
    
    /**
     * 公司固定电话
     */
    private String companyFixedPhone;
    
    /**
     * 合作业务范围（JSON 数组）
     */
    private String businessScope;
    
    /**
     * 合作区域（JSON 数组）
     */
    private String cooperationArea;
    
    /**
     * 合作模式
     */
    private String cooperationMode;
}
