package com.nh.nsight.marketing.etc.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransactionIoMapper {
    int insertLog(Map<String, Object> parameter);

    Map<String, Object> selectLogById(Map<String, Object> parameter);

    Map<String, Object> selectLogByGuid(Map<String, Object> parameter);

    long countLogList(Map<String, Object> parameter);

    int deleteAllLogs();

    List<Map<String, Object>> selectLogList(Map<String, Object> parameter);
}
