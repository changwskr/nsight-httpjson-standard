package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmAuditLogService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmAuditLogService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>(OmBodySupport.searchCriteria(body));
        rule.normalizePaging(criteria);
        copyIfPresent(body, criteria, "userId", "branchId", "customerNo", "functionId", "resultStatus", "fromDate", "toDate");

        List<Map<String, Object>> rows = dao.searchAuditLogs(criteria);
        int totalCount = dao.countAuditLogs(criteria);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "감사로그 조회");
        result.put("pageNo", criteria.get("pageNo"));
        result.put("pageSize", criteria.get("pageSize"));
        result.put("totalCount", totalCount);
        result.put("rows", rows);
        return result;
    }

    private void copyIfPresent(Map<String, Object> body, Map<String, Object> criteria, String... keys) {
        for (String key : keys) {
            String value = OmBodySupport.stringValue(body, key);
            if (value != null) {
                criteria.put(key, value);
            }
        }
    }
}
