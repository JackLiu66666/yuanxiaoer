package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuanxiaoer.yuanxiaoer.admin.entity.Hospital;
import com.yuanxiaoer.yuanxiaoer.admin.mapper.HospitalMapper;
import com.yuanxiaoer.yuanxiaoer.admin.service.HospitalService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 医院服务实现类
 */
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Override
    public List<String> getProvincesWithAgency() {
        return this.baseMapper.getProvincesWithAgency();
    }

    @Override
    public List<String> getCitiesByProvince(String province) {
        return this.baseMapper.getCitiesByProvince(province);
    }

    @Override
    public List<Hospital> getHospitalsByCity(String city) {
        return this.baseMapper.getHospitalsByCity(city);
    }
}
