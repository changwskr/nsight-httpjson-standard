package com.nh.nsight.marketing.bc.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BcSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
