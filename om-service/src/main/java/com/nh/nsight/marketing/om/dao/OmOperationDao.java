package com.nh.nsight.marketing.om.dao;

import com.nh.nsight.marketing.om.mapper.OmOperationMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class OmOperationDao {
    private final OmOperationMapper mapper;

    public OmOperationDao(OmOperationMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Object> selectTxSummary(String baseDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("baseDate", baseDate);
        return mapper.selectTxSummary(params);
    }

    public List<Map<String, Object>> selectErrorTop(String baseDate) {
        Map<String, Object> params = new HashMap<>();
        params.put("baseDate", baseDate);
        return mapper.selectErrorTop(params);
    }

    public List<Map<String, Object>> selectApStatus() {
        return mapper.selectApStatus();
    }

    public List<Map<String, Object>> selectDbStatus() {
        return mapper.selectDbStatus();
    }

    public List<Map<String, Object>> selectDeployStatus() {
        return mapper.selectDeployStatus();
    }

    public int selectActiveSessionCount() {
        Integer count = mapper.selectActiveSessionCount();
        return count == null ? 0 : count;
    }

    public List<Map<String, Object>> searchTransactionLogs(Map<String, Object> criteria) {
        return mapper.searchTransactionLogs(criteria);
    }

    public int countTransactionLogs(Map<String, Object> criteria) {
        return mapper.countTransactionLogs(criteria);
    }

    public List<Map<String, Object>> searchServiceCatalog(Map<String, Object> criteria) {
        return mapper.searchServiceCatalog(criteria);
    }

    public List<Map<String, Object>> searchUsers(Map<String, Object> criteria) {
        return mapper.searchUsers(criteria);
    }

    public List<Map<String, Object>> searchMenus(Map<String, Object> criteria) {
        return mapper.searchMenus(criteria);
    }

    public List<Map<String, Object>> searchAuthGroups(Map<String, Object> criteria) {
        return mapper.searchAuthGroups(criteria);
    }

    public List<Map<String, Object>> searchAuditLogs(Map<String, Object> criteria) {
        return mapper.searchAuditLogs(criteria);
    }

    public int countAuditLogs(Map<String, Object> criteria) {
        return mapper.countAuditLogs(criteria);
    }

    public List<Map<String, Object>> searchErrorCodes(Map<String, Object> criteria) {
        return mapper.searchErrorCodes(criteria);
    }

    public List<Map<String, Object>> searchBatchJobs(Map<String, Object> criteria) {
        return mapper.searchBatchJobs(criteria);
    }

    public List<Map<String, Object>> searchBatchHistories(Map<String, Object> criteria) {
        return mapper.searchBatchHistories(criteria);
    }

    public int countBatchHistories(Map<String, Object> criteria) {
        return mapper.countBatchHistories(criteria);
    }

    public List<Map<String, Object>> searchSystemConfigs(Map<String, Object> criteria) {
        return mapper.searchSystemConfigs(criteria);
    }

    public List<Map<String, Object>> searchFileDownloadLogs(Map<String, Object> criteria) {
        return mapper.searchFileDownloadLogs(criteria);
    }

    public int countFileDownloadLogs(Map<String, Object> criteria) {
        return mapper.countFileDownloadLogs(criteria);
    }

    public List<Map<String, Object>> searchCommonCodes(Map<String, Object> criteria) {
        return mapper.searchCommonCodes(criteria);
    }

    public int mergeCommonCode(Map<String, Object> row) {
        return mapper.mergeCommonCode(row);
    }

    public List<Map<String, Object>> searchFunctionAuths(Map<String, Object> criteria) {
        return mapper.searchFunctionAuths(criteria);
    }

    public List<Map<String, Object>> searchDataAuths(Map<String, Object> criteria) {
        return mapper.searchDataAuths(criteria);
    }

    public List<Map<String, Object>> searchAuthHistories(Map<String, Object> criteria) {
        return mapper.searchAuthHistories(criteria);
    }

    public int countAuthHistories(Map<String, Object> criteria) {
        return mapper.countAuthHistories(criteria);
    }

    public int insertAuthHistory(Map<String, Object> row) {
        return mapper.insertAuthHistory(row);
    }

    public Map<String, Object> selectErrorCodeByCode(String errorCode) {
        return mapper.selectErrorCodeByCode(errorCode);
    }

    public int mergeErrorCode(Map<String, Object> row) {
        return mapper.mergeErrorCode(row);
    }

    public Map<String, Object> selectBatchJobById(String jobId) {
        return mapper.selectBatchJobById(jobId);
    }

    public int insertBatchHistory(Map<String, Object> row) {
        return mapper.insertBatchHistory(row);
    }

    public List<Map<String, Object>> searchCacheStatus(Map<String, Object> criteria) {
        return mapper.searchCacheStatus(criteria);
    }

    public int deleteCache(Map<String, Object> criteria) {
        return mapper.deleteCache(criteria);
    }

    public int insertAuditLog(Map<String, Object> row) {
        return mapper.insertAuditLog(row);
    }
}
