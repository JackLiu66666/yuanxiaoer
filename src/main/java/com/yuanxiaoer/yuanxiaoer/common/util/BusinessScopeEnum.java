package com.yuanxiaoer.yuanxiaoer.common.util;

/**
 * 业务范围枚举
 */
public class BusinessScopeEnum {
    public static final int HOSPITAL_ACCOMPANY_DIAGNOSIS = 1; // 院陪诊
    public static final int HOSPITAL_ACCOMPANY_CARE = 2;      // 院陪护
    public static final int HOSPITAL_OFFICE = 3;              // 院办办
    public static final int HOSPITAL_INSURANCE = 4;           // 院保保
    public static final int HOSPITAL_ADVANCE = 5;             // 院垫垫
    public static final int HOSPITAL_VISIT = 6;               // 院探探
    
    /**
     * 根据数字获取业务名称
     */
    public static String getName(int code) {
        switch (code) {
            case 1: return "院陪诊";
            case 2: return "院陪护";
            case 3: return "院办办";
            case 4: return "院保保";
            case 5: return "院垫垫";
            case 6: return "院探探";
            default: return "未知";
        }
    }
}
