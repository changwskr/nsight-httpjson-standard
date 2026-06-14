package com.nh.nsight.marketing.bd.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BdSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
