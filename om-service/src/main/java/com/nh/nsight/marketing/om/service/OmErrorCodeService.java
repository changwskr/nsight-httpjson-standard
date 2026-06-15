package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import com.nh.nsight.marketing.om.support.OmChangeRecorder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class OmErrorCodeService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;
    private final OmChangeRecorder recorder;

    public OmErrorCodeService(OmOperationRule rule, OmOperationDao dao, OmChangeRecorder recorder) {
        this.rule = rule;
        this.dao = dao;
        this.recorder = recorder;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        putIfPresent(body, criteria, "errorCode", "errorCategory", "useYn");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "오류코드 / 메시지 관리");
        List<Map<String, Object>> rows = dao.searchErrorCodes(criteria);
        result.put("rows", rows);
        result.put("totalCount", rows.size());
        return result;
    }

    public Map<String, Object> save(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        rule.requireField(body, "errorCode");
        rule.requireField(body, "errorCategory");
        rule.requireReason(body, "changeReason");

        String errorCode = OmBodySupport.stringValue(body, "errorCode");
        Map<String, Object> before = dao.selectErrorCodeByCode(errorCode);

        Map<String, Object> row = new HashMap<>();
        row.put("errorCode", errorCode);
        row.put("errorCategory", OmBodySupport.stringValue(body, "errorCategory"));
        row.put("userMessage", OmBodySupport.stringValue(body, "userMessage"));
        row.put("operatorMessage", OmBodySupport.stringValue(body, "operatorMessage"));
        row.put("actionGuide", OmBodySupport.stringValue(body, "actionGuide"));
        row.put("notifyTarget", OmBodySupport.stringValue(body, "notifyTarget"));
        row.put("useYn", OmBodySupport.stringValue(body, "useYn") != null ? body.get("useYn") : "Y");

        dao.mergeErrorCode(row);
        recorder.recordAuthHistory(context, "ERROR_CODE", errorCode,
                before == null ? null : String.valueOf(before.get("userMessage")),
                String.valueOf(row.get("userMessage")),
                OmBodySupport.stringValue(body, "changeReason"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "오류코드 저장");
        result.put("saved", true);
        result.put("errorCode", errorCode);
        result.put("mode", before == null ? "REGISTER" : "UPDATE");
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
