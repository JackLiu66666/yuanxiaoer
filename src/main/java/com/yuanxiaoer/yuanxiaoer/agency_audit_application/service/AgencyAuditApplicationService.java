package com.yuanxiaoer.yuanxiaoer.agency_audit_application.service;

import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.util.CooperationAreaItem;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.CityVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.HospitalSelectVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.vo.ProvinceVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 代理商申请服务接口
 */
public interface AgencyAuditApplicationService {
    // 提交申请（含文件）
    String submitApplication(AgencyAuditApplication application, MultipartFile sealFile, HttpSession session);
    
    // 检查代理商是否已提交申请
    boolean hasApplied(Long agencyId);

    //根据 ID 查询申请表单详情
    AgencyAuditApplication getApplicationById(Long id);
    
    /**
     * 查询所有有代理商的省份
     * @return 省份列表
     */
    List<ProvinceVO> getProvinces();
    
    /**
     * 根据省份查询城市
     * @param province 省份
     * @return 城市列表
     */
    List<CityVO> getCitiesByProvince(String province);
    
    /**
     * 根据城市查询医院
     * @param city 城市
     * @return 医院列表
     */
    List<HospitalSelectVO> getHospitalsByCity(String city);
    
    /**
     * 验证业务范围代码是否有效
     * @param codes 业务代码列表
     * @return 验证结果
     */
    boolean validateBusinessScope(List<Integer> codes);
    
    /**
     * 验证合作区域数据是否有效
     * @param items 合作区域列表
     * @return 验证结果
     */
    boolean validateCooperationArea(List<CooperationAreaItem> items);
}
