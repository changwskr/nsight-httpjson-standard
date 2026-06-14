package com.nh.nsight.marketing.pc.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PcSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
