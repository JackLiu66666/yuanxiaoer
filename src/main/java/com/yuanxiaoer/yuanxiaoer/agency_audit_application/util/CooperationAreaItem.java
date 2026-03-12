package com.yuanxiaoer.yuanxiaoer.agency_audit_application.util;

import lombok.Data;
import java.util.List;

/**
 * 合作区域数据项
 */
@Data
public class CooperationAreaItem {
    private String province;      // 省份
    private String city;          // 城市
    private List<Long> hospitalIds; // 医院 ID 列表（支持多个医院）
    
    /**
     * 是否为全市（hospitalIds 为空或 null 表示全市）
     */
    public boolean isCityWide() {
        return hospitalIds == null || hospitalIds.isEmpty();
    }
    
    /**
     * 格式化显示
     */
    public String getDisplayText() {
        if (isCityWide()) {
            return province + "-" + city + "-全市";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(province).append("-").append(city).append("-医院:");
            for (int i = 0; i < hospitalIds.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(hospitalIds.get(i));
            }
            return sb.toString();
        }
    }
    
    /**
     * 获取医院数量
     */
    public int getHospitalCount() {
        if (hospitalIds == null) {
            return 0;
        }
        return hospitalIds.size();
    }
}
