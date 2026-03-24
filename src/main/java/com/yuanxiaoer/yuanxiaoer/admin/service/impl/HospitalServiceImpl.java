package com.yuanxiaoer.yuanxiaoer.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    
    @Override
    public List<Hospital> searchHospitalsByName(String keyword) {
        return this.baseMapper.searchHospitalsByName(keyword);
    }
    
    @Override
    public IPage<Hospital> searchHospitalsByName(String keyword, Page<Hospital> page) {
        return this.baseMapper.searchHospitalsByName(page, keyword);
    }
    
    @Override
    public List<String> getAllCitiesByProvince(String province) {
        return this.baseMapper.getAllCitiesByProvince(province);
    }
    
    @Override
    public IPage<Hospital> getHospitalsByCityAndLevel(String city, String hospitalLevel, Page<Hospital> page) {
        return this.baseMapper.getHospitalsByCityAndLevel(page, city, hospitalLevel);
    }
}
