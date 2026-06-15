package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmAuthHistoryService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmAuthHistoryService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>(OmBodySupport.searchCriteria(body));
        rule.normalizePaging(criteria);
        putIfPresent(body, criteria, "targetType", "changedBy");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "권한이력 조회");
        result.put("pageNo", criteria.get("pageNo"));
        result.put("pageSize", criteria.get("pageSize"));
        result.put("totalCount", dao.countAuthHistories(criteria));
        result.put("rows", dao.searchAuthHistories(criteria));
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
