package com.yuanxiaoer.yuanxiaoer.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务范围工具类
 */
public class BusinessScopeUtil {
    
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

