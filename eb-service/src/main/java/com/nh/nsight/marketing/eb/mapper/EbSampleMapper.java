package com.nh.nsight.marketing.eb.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EbSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
