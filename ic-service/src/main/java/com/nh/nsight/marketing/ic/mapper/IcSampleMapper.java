package com.nh.nsight.marketing.ic.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IcSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);
}
