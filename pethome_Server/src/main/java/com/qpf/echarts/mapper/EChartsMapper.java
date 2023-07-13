package com.qpf.echarts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qpf.echarts.vo.ECharsShopVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EChartsMapper extends BaseMapper {
    List<ECharsShopVo> getShopEcharts();

}
