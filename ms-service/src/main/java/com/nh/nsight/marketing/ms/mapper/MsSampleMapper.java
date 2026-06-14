package com.nh.nsight.marketing.ms.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MsSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
