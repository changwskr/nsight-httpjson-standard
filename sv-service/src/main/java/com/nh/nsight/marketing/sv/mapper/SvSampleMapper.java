package com.nh.nsight.marketing.sv.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SvSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
