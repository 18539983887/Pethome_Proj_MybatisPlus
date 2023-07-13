package com.qpf.echarts.service.Impl;


import com.qpf.echarts.mapper.EChartsMapper;
import com.qpf.echarts.service.IEChartsService;
import com.qpf.echarts.vo.ECharsShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EChartsServiceImpl implements IEChartsService {
    @Autowired
    private EChartsMapper eChartsMapper;
    @Override
    public List<ECharsShopVo> getShopEcharts() {

        return eChartsMapper.getShopEcharts();

    }
}
