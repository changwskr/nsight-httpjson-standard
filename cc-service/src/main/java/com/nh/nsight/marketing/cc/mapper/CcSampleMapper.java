package com.nh.nsight.marketing.cc.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CcSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
