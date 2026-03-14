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
    
    // 申请唯一 ID（自定义生成）
    private String applyId;
    
    // 关联代理商 ID
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
    
    // 许可证有效期 - 开始
    private LocalDate licenseValidStart;
    
    // 许可证有效期 - 结束
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
    
    // 合作业务范围（JSON 数组，存储 1-6 的数字，分别代表：1-院陪诊，2-院陪护，3-院办办，4-院保保，5-院垫垫，6-院探探）
    private String businessScope;
    
    // 合作区域（JSON 数组，每个对象包含 province、city、hospitalIds 字段，
    // 例如：[{"province":"广东省","city":"广州市","hospitalIds":[10,11,12]},{"province":"广东省","city":"深圳市","hospitalIds":[]}]，
    // hospitalIds 为空数组或 null 表示全市）
    private String cooperationArea;
    
    // 合作模式
    private String cooperationMode;
    
    // 资料真实性承诺（1-同意）
    private Integer authenticityCommitment;
    
    // 盖章文件路径
    private String sealFilePath;
    
    // 申请日期
    private LocalDate applyDate;
    
    // 审核状态：0-未审核，1-待审核，2-审核通过，3-审核不通过（默认为 1-待审核）
    private Integer auditStatus;
    
    // 软删除标识（保留字段，暂不使用）
    @TableLogic
    private Integer isDeleted;
    
    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
