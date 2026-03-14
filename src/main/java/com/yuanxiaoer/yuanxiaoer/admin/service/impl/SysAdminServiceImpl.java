package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.dto.LoginDTO;
import com.yuanxiaoer.yuanxiaoer.admin.entity.AgencyBusinessScopeRelation;
import com.yuanxiaoer.yuanxiaoer.admin.entity.HospitalAgencyRelation;
import com.yuanxiaoer.yuanxiaoer.admin.entity.SysAdmin;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.SysAdminMapper;
import com.yuanxiaoer.yuanxiaoer.admin.service.AgencyInfoService;
import com.yuanxiaoer.yuanxiaoer.admin.service.SysAdminService;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.AgencyBusinessScopeRelationMapper;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.HospitalAgencyRelationMapper;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.HospitalMapper;
import com.yuanxiaoer.yuanxiaoer.admin.vo.AgencyApplyVO;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.entity.AgencyAuditApplication;
import com.yuanxiaoer.yuanxiaoer.agency_audit_application.mapper.AgencyAuditApplicationMapper;
import com.yuanxiaoer.yuanxiaoer.agency_login.mapper.AgencyMapper;
import com.yuanxiaoer.yuanxiaoer.common.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * 管理员服务实现类
 */
@Service
public class SysAdminServiceImpl extends ServiceImpl<SysAdminMapper, SysAdmin> implements SysAdminService {

    /**
     * 管理员登录
     * @param loginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public Result<SysAdmin> login(LoginDTO loginDTO) {
        // 1. 校验参数
        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return Result.error("账号不能为空");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            return Result.error("密码不能为空");
        }

        // 2. 根据账号查询管理员
        LambdaQueryWrapper<SysAdmin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysAdmin::getUsername, loginDTO.getUsername());
        SysAdmin sysAdmin = this.getOne(queryWrapper);

        // 3. 校验账号是否存在
        if (sysAdmin == null) {
            return Result.error("账号不存在");
        }

        // 4. 校验密码（实际项目要加密后对比，这里先明文）
        if (!sysAdmin.getPassword().equals(loginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        // 5. 登录成功（实际项目这里要生成token，这里简化）
        return Result.success(sysAdmin);
    }

    /**
     * 获取申请列表
     * @param page 分页参数
     * @return 申请列表
     */
    @Autowired
    private AgencyAuditApplicationMapper agencyAuditApplicationMapper;
    
    @Autowired
    private AgencyMapper agencyMapper;
    
    @Autowired
    private HospitalAgencyRelationMapper hospitalAgencyRelationMapper;
    
    @Autowired
    private AgencyBusinessScopeRelationMapper agencyBusinessScopeRelationMapper;
    
    @Autowired
    private HospitalMapper hospitalMapper;
    
    @Autowired
    private AgencyInfoService agencyInfoService;
    
    @Value("${upload.base-path}")
    private String uploadBasePath;

    @Override
    public IPage<AgencyApplyVO> getApplyList(Page<AgencyApplyVO> page) {
        return agencyAuditApplicationMapper.selectApplyPage(page);
    }

    /**
     * 审核通过
     * @param id 申请 ID
     * @return 审核结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> approveApplication(Long id) {
        try {
            // 1. 查询申请信息
            LambdaQueryWrapper<AgencyAuditApplication> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AgencyAuditApplication::getId, id);
            AgencyAuditApplication application = agencyAuditApplicationMapper.selectOne(queryWrapper);
            
            if (application == null) {
                return Result.error("申请不存在");
            }
            
            Long agencyId = application.getAgencyId();
            
            // 2. 更新申请审核状态为审核通过（2）
            LambdaUpdateWrapper<AgencyAuditApplication> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(AgencyAuditApplication::getId, application.getId())
                        .set(AgencyAuditApplication::getAuditStatus, 2);
            agencyAuditApplicationMapper.update(null, updateWrapper);
            
            
            // 3. 更新代理商审核状态为通过（2）
            agencyMapper.updateAuditStatus(agencyId, 2);
            
            // 4. 处理合作区域，添加医院与代理商关系
            if (application.getCooperationArea() != null && !application.getCooperationArea().isEmpty()) {
                try {
                    // 解析合作区域 JSON
                    List<Map<String, Object>> areas = JSON.parseObject(
                        application.getCooperationArea(),
                        new com.alibaba.fastjson.TypeReference<List<Map<String, Object>>>() {}
                    );
                    
                    // 遍历每个区域，添加医院与代理商关系
                    for (Map<String, Object> area : areas) {
                        String province = (String) area.get("province");
                        String city = (String) area.get("city");
                        Object hospitalIdsObj = area.get("hospitalIds");
                        
                        List<Long> hospitalIds = new java.util.ArrayList<>();
                        
                        // 判断是否为空数组，如果是空数组则查询该城市的所有医院
                        if (hospitalIdsObj instanceof List) {
                            // 使用流式处理进行安全的类型转换
                            hospitalIds = ((List<?>) hospitalIdsObj)
                                .stream()
                                .map(hospitalIdObj -> {
                                    if (hospitalIdObj instanceof Number) {
                                        return ((Number) hospitalIdObj).longValue();
                                    } else if (hospitalIdObj instanceof String) {
                                        try {
                                            return Long.parseLong((String) hospitalIdObj);
                                        } catch (NumberFormatException e) {
                                            throw new IllegalArgumentException("Invalid hospitalId format: " + hospitalIdObj);
                                        }
                                    } else {
                                        throw new IllegalArgumentException("Invalid hospitalId type: " + (hospitalIdObj != null ? hospitalIdObj.getClass().getName() : "null"));
                                    }
                                })
                                .collect(java.util.stream.Collectors.toList());
                            
                            // 如果 hospitalIds 为空数组，查询该城市的所有医院
                            if (hospitalIds.isEmpty() && city != null && !city.isEmpty()) {
                                // 查询该城市的所有医院
                                LambdaQueryWrapper<com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital> hospitalQuery = new LambdaQueryWrapper<>();
                                hospitalQuery.eq(com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital::getCity, city);
                                if (province != null && !province.isEmpty()) {
                                    hospitalQuery.eq(com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital::getProvince, province);
                                }
                                List<com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital> allHospitals = hospitalMapper.selectList(hospitalQuery);
                                
                                // 将查询到的医院 ID 添加到列表中
                                hospitalIds = allHospitals.stream()
                                    .map(com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital::getId)
                                    .collect(java.util.stream.Collectors.toList());
                            }
                        }
                        
                        // 添加医院与代理商关系
                        for (Long hospitalId : hospitalIds) {
                            // 检查是否已存在，避免重复添加
                            LambdaQueryWrapper<HospitalAgencyRelation> relationQuery = new LambdaQueryWrapper<>();
                            relationQuery.eq(HospitalAgencyRelation::getHospitalId, hospitalId)
                                        .eq(HospitalAgencyRelation::getAgencyId, agencyId);
                            long count = hospitalAgencyRelationMapper.selectCount(relationQuery);
                            
                            if (count == 0) {
                                // 添加新的关系
                                HospitalAgencyRelation relation = new HospitalAgencyRelation();
                                relation.setHospitalId(hospitalId);
                                relation.setAgencyId(agencyId);
                                hospitalAgencyRelationMapper.insert(relation);
                            }
                        }
                    }
                } catch (Exception e) {
                    // 记录错误但不影响主流程
                    System.err.println("处理合作区域失败：" + e.getMessage());
                }
            }
            
            // 5. 处理业务范围，添加代理商与业务范围关系
            if (application.getBusinessScope() != null && !application.getBusinessScope().isEmpty()) {
                try {
                    // 使用流式处理进行安全的类型转换
                    List<?> businessScopesRaw = JSON.parseArray(application.getBusinessScope());
                    List<Integer> businessScopes = businessScopesRaw.stream()
                        .map(obj -> {
                            if (obj instanceof Number) {
                                return ((Number) obj).intValue();
                            } else if (obj instanceof String) {
                                try {
                                    return Integer.parseInt((String) obj);
                                } catch (NumberFormatException e) {
                                    throw new IllegalArgumentException("Invalid businessScope format: " + obj);
                                }
                            } else {
                                throw new IllegalArgumentException("Invalid businessScope type: " + (obj != null ? obj.getClass().getName() : "null"));
                            }
                        })
                        .collect(java.util.stream.Collectors.toList());
                    
                    for (Integer businessScope : businessScopes) {
                        // 检查是否已存在，避免重复添加
                        LambdaQueryWrapper<AgencyBusinessScopeRelation> relationQuery = new LambdaQueryWrapper<>();
                        relationQuery.eq(AgencyBusinessScopeRelation::getAgencyId, agencyId)
                                    .eq(AgencyBusinessScopeRelation::getBusinessScope, businessScope);
                        long count = agencyBusinessScopeRelationMapper.selectCount(relationQuery);
                        
                        if (count == 0) {
                            // 添加新的关系
                            AgencyBusinessScopeRelation relation = new AgencyBusinessScopeRelation();
                            relation.setAgencyId(agencyId);
                            relation.setBusinessScope(businessScope);
                            agencyBusinessScopeRelationMapper.insert(relation);
                        }
                    }
                } catch (Exception e) {
                    // 记录错误但不影响主流程
                    System.err.println("处理业务范围失败：" + e.getMessage());
                }
            }
            
            // 6. 创建代理商信息（从申请表复制数据）
            try {
                boolean created = agencyInfoService.createFromApplication(application, uploadBasePath);
                if (!created) {
                    System.err.println("创建代理商信息失败，agencyId: " + agencyId);
                }
            } catch (Exception e) {
                // 记录错误但不影响主流程
                System.err.println("创建代理商信息失败：" + e.getMessage());
            }
            
            return Result.success("审核通过");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("审核失败：" + e.getMessage());
        }
    }

    /**
     * 审核不通过
     * @param id 申请 ID
     * @return 审核结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> rejectApplication(Long id) {
        try {
            // 1. 查询申请信息
            LambdaQueryWrapper<AgencyAuditApplication> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AgencyAuditApplication::getId, id);
            AgencyAuditApplication application = agencyAuditApplicationMapper.selectOne(queryWrapper);
            
            if (application == null) {
                return Result.error("申请不存在");
            }
            
            Long agencyId = application.getAgencyId();
            
            // 2. 更新申请审核状态为审核不通过（3）
            LambdaUpdateWrapper<AgencyAuditApplication> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(AgencyAuditApplication::getId, application.getId())
                        .set(AgencyAuditApplication::getAuditStatus, 3);
            agencyAuditApplicationMapper.update(null, updateWrapper);
            
            
            // 3. 更新代理商审核状态为不通过（3）
            agencyMapper.updateAuditStatus(agencyId, 3);
            
            return Result.success("审核不通过");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("审核失败：" + e.getMessage());
        }
    }

}
