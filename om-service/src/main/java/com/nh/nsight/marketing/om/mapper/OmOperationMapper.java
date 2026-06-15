package com.nh.nsight.marketing.om.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OmOperationMapper {
    Map<String, Object> selectTxSummary(Map<String, Object> params);

    List<Map<String, Object>> selectErrorTop(Map<String, Object> params);

    List<Map<String, Object>> selectApStatus();

    List<Map<String, Object>> selectDbStatus();

    List<Map<String, Object>> selectDeployStatus();

    Integer selectActiveSessionCount();

    List<Map<String, Object>> searchTransactionLogs(Map<String, Object> params);

    int countTransactionLogs(Map<String, Object> params);

    List<Map<String, Object>> searchServiceCatalog(Map<String, Object> params);

    List<Map<String, Object>> searchUsers(Map<String, Object> params);

    List<Map<String, Object>> searchMenus(Map<String, Object> params);

    List<Map<String, Object>> searchAuthGroups(Map<String, Object> params);

    List<Map<String, Object>> searchAuditLogs(Map<String, Object> params);

    int countAuditLogs(Map<String, Object> params);

    List<Map<String, Object>> searchErrorCodes(Map<String, Object> params);

    List<Map<String, Object>> searchBatchJobs(Map<String, Object> params);

    List<Map<String, Object>> searchBatchHistories(Map<String, Object> params);

    int countBatchHistories(Map<String, Object> params);

    List<Map<String, Object>> searchSystemConfigs(Map<String, Object> params);

    List<Map<String, Object>> searchFileDownloadLogs(Map<String, Object> params);

    int countFileDownloadLogs(Map<String, Object> params);

    List<Map<String, Object>> searchCommonCodes(Map<String, Object> params);

    int mergeCommonCode(Map<String, Object> params);

    List<Map<String, Object>> searchFunctionAuths(Map<String, Object> params);

    List<Map<String, Object>> searchDataAuths(Map<String, Object> params);

    List<Map<String, Object>> searchAuthHistories(Map<String, Object> params);

    int countAuthHistories(Map<String, Object> params);

    int insertAuthHistory(Map<String, Object> params);

    Map<String, Object> selectErrorCodeByCode(String errorCode);

    int mergeErrorCode(Map<String, Object> params);

    Map<String, Object> selectBatchJobById(String jobId);

    int insertBatchHistory(Map<String, Object> params);

    List<Map<String, Object>> searchCacheStatus(Map<String, Object> params);

    int deleteCache(Map<String, Object> params);

    int insertAuditLog(Map<String, Object> params);
}
