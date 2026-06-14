package com.nh.nsight.marketing.pd.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PdSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
