package com.yuanxiaoer.yuanxiaoer.common.util;

import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.List;

/**
 * 合作区域工具类
 */
public class CooperationAreaUtil {
    /**
     * 将合作区域列表转为 JSON 字符串
     * @param items 合作区域列表
     * @return JSON 字符串
     */
    public static String toJson(List<CooperationAreaItem> items) {
        if (items == null || items.isEmpty()) {
            return "[]";
        }
        return JSON.toJSONString(items);
    }
    
    /**
     * 将 JSON 字符串解析为合作区域列表
     * @param json JSON 字符串
     * @return 合作区域列表
     */
    public static List<CooperationAreaItem> fromJson(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return JSON.parseArray(json, CooperationAreaItem.class);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
    
    /**
     * 统计总医院数量
     * @param items 合作区域列表
     * @return 医院总数
     */
    public static int getTotalHospitalCount(List<CooperationAreaItem> items) {
        if (items == null) {
            return 0;
        }
        int count = 0;
        for (CooperationAreaItem item : items) {
            count += item.getHospitalCount();
        }
        return count;
    }
}
