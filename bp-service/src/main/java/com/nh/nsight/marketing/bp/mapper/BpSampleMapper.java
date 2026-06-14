package com.nh.nsight.marketing.bp.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BpSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
