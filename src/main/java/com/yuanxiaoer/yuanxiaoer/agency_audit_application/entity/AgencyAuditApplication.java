package com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("agency_audit_application")
public class AgencyAuditApplication {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    // 申请唯一ID（自定义生成）
    private String applyId;
    
    // 关联代理商ID
    private Long agencyId;
    
    // 机构全称
    private String agencyFullName;
    
    // 统一社会信用代码
    private String socialCreditCode;
    
    // 注册地址
    private String registeredAddress;
    
    // 机构类型
    private String agencyType;
    
    // 经营许可证/备案号
    private String businessLicenseNo;
    
    // 许可证有效期-开始
    private LocalDate licenseValidStart;
    
    // 许可证有效期-结束
    private LocalDate licenseValidEnd;
    
    // 法定代表人姓名
    private String legalPersonName;
    
    // 法定代表人身份证号
    private String legalPersonIdCard;
    
    // 法定代表人手机号
    private String legalPersonPhone;
    
    // 对接负责人姓名
    private String contactPersonName;
    
    // 对接负责人职务
    private String contactPersonPosition;
    
    // 对接负责人手机号
    private String contactPersonPhone;
    
    // 对接负责人办公电话
    private String contactPersonOfficePhone;
    
    // 对接负责人邮箱
    private String contactPersonEmail;
    
    // 公司固定电话
    private String companyFixedPhone;
    
    // 合作业务范围
    private String businessScope;
    
    // 合作区域
    private String cooperationArea;
    
    // 合作模式
    private String cooperationMode;
    
    // 资料真实性承诺（1-同意）
    private Integer authenticityCommitment;
    
    // 盖章文件路径
    private String sealFilePath;
    
    // 申请日期
    private LocalDate applyDate;
    
    // 软删除标识
    @TableLogic
    private Integer isDeleted;
    
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
