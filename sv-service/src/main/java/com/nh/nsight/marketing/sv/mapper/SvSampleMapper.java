package com.nh.nsight.marketing.sv.mapper;

import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SvSampleMapper {
    Map<String, Object> selectSample(Map<String, Object> parameter);

    default Map<String, Object> selectSampleWithLogging(Map<String, Object> parameter) {
        System.out.println("====================================================================[SvSampleMapper.selectSample] start");
        System.out.println("parameter: " + parameter);
        Map<String, Object> result = selectSample(parameter);
        System.out.println("result: " + result);
        System.out.println("====================================================================[SvSampleMapper.selectSample] end");
        return result;
    }
}
