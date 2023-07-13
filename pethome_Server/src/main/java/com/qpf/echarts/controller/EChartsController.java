package com.qpf.echarts.controller;

import com.qpf.echarts.service.IEChartsService;
import com.qpf.echarts.vo.ECharsShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/echarts")
public class EChartsController {

    @Autowired
    private IEChartsService echartsService;

    @GetMapping("/shop")
    public List<ECharsShopVo> echartsDataShops(){
        return echartsService.getShopEcharts();
    }
}
