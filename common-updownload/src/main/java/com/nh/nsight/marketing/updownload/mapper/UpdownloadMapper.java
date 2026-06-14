package com.nh.nsight.marketing.updownload.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UpdownloadMapper {
    int insertFile(Map<String, Object> parameter);

    Map<String, Object> selectFileById(Map<String, Object> parameter);

    List<Map<String, Object>> selectFileList(Map<String, Object> parameter);
}
