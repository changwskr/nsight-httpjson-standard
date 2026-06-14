package com.nh.nsight.marketing.cm.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CmSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
