package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import com.nh.nsight.marketing.om.support.OmChangeRecorder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmCacheService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;
    private final OmChangeRecorder recorder;

    public OmCacheService(OmOperationRule rule, OmOperationDao dao, OmChangeRecorder recorder) {
        this.rule = rule;
        this.dao = dao;
        this.recorder = recorder;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        putIfPresent(body, criteria, "cacheName");
        List<Map<String, Object>> rows = dao.searchCacheStatus(criteria);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "Cache 조회");
        result.put("rows", rows);
        result.put("totalCount", rows.size());
        return result;
    }

    public Map<String, Object> delete(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        rule.requireField(body, "cacheName");
        rule.requireReason(body, "deleteReason");

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("cacheName", OmBodySupport.stringValue(body, "cacheName"));
        String cacheKey = OmBodySupport.stringValue(body, "cacheKey");
        if (cacheKey != null) {
            criteria.put("cacheKey", cacheKey);
        }

        int deleted = dao.deleteCache(criteria);
        if (deleted == 0) {
            throw new BusinessException("E-OM-BIZ-0002", "삭제할 Cache 항목이 없습니다.");
        }

        String reason = OmBodySupport.stringValue(body, "deleteReason");
        recorder.recordAdminAudit(context, "CACHE_DELETE", "Cache 삭제", reason, "SUCCESS");
        recorder.recordAuthHistory(context, "CACHE", String.valueOf(criteria.get("cacheName")),
                "exists", "deleted:" + deleted, reason);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "Cache 삭제");
        result.put("deletedCount", deleted);
        result.put("cacheName", criteria.get("cacheName"));
        return result;
    }

    private void putIfPresent(Map<String, Object> body, Map<String, Object> criteria, String... keys) {
        for (String key : keys) {
            String value = OmBodySupport.stringValue(body, key);
            if (value != null) {
                criteria.put(key, value);
            }
        }
    }
}
