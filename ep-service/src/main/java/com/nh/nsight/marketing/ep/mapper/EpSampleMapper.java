package com.nh.nsight.marketing.ep.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EpSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
