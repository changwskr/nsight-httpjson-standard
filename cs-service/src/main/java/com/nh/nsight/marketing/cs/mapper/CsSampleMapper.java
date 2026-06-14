package com.nh.nsight.marketing.cs.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CsSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
