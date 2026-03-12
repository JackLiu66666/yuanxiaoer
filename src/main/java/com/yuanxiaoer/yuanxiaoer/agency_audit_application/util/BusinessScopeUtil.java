package com.yuanxiaoer.yuanxiaoer.agency_audit_application.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务范围工具类
 */
public class BusinessScopeUtil {
    
    // 业务范围枚举值
    private static final int HOSPITAL_ACCOMPANY_DIAGNOSIS = 1; // 院陪诊
    private static final int HOSPITAL_ACCOMPANY_CARE = 2;      // 院陪护
    private static final int HOSPITAL_OFFICE = 3;              // 院办办
    private static final int HOSPITAL_INSURANCE = 4;           // 院保保
    private static final int HOSPITAL_ADVANCE = 5;             // 院垫垫
    private static final int HOSPITAL_VISIT = 6;               // 院探探
    
    /**
     * 根据数字获取业务名称
     */
    private static String getName(int code) {
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
    /**
     * 将选中的业务代码列表转为 JSON 字符串
     * @param codes 业务代码列表，如 [1,2,3]
     * @return JSON 字符串
     */
    public static String toJson(List<Integer> codes) {
        if (codes == null || codes.isEmpty()) {
            return "[]";
        }
        return JSON.toJSONString(codes);
    }
    
    /**
     * 将 JSON 字符串解析为业务代码列表
     * @param json JSON 字符串
     * @return 业务代码列表
     */
    public static List<Integer> fromJson(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            JSONArray array = JSON.parseArray(json);
            List<Integer> result = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                result.add(array.getInteger(i));
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 获取业务名称列表
     * @param codes 业务代码列表
     * @return 业务名称列表
     */
    public static List<String> getNames(List<Integer> codes) {
        List<String> names = new ArrayList<>();
        for (Integer code : codes) {
            names.add(BusinessScopeEnum.getName(code));
        }
        return names;
    }
}

