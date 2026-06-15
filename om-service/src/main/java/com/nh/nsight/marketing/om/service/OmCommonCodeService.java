package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
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
public class OmCommonCodeService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;
    private final OmChangeRecorder recorder;

    public OmCommonCodeService(OmOperationRule rule, OmOperationDao dao, OmChangeRecorder recorder) {
        this.rule = rule;
        this.dao = dao;
        this.recorder = recorder;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        Map<String, Object> criteria = new HashMap<>();
        putIfPresent(body, criteria, "codeGroup", "useYn");
        List<Map<String, Object>> rows = dao.searchCommonCodes(criteria);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "공통코드 관리");
        result.put("rows", rows);
        result.put("totalCount", rows.size());
        return result;
    }

    public Map<String, Object> save(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        rule.requireField(body, "codeGroup");
        rule.requireField(body, "code");
        rule.requireField(body, "codeName");
        rule.requireReason(body, "changeReason");

        Map<String, Object> row = new HashMap<>();
        row.put("codeGroup", OmBodySupport.stringValue(body, "codeGroup"));
        row.put("code", OmBodySupport.stringValue(body, "code"));
        row.put("codeName", OmBodySupport.stringValue(body, "codeName"));
        row.put("sortOrder", OmBodySupport.intValue(body, "sortOrder", 0));
        row.put("useYn", OmBodySupport.stringValue(body, "useYn") != null ? body.get("useYn") : "Y");
        row.put("description", OmBodySupport.stringValue(body, "description"));

        dao.mergeCommonCode(row);
        recorder.recordAuthHistory(context, "COMMON_CODE",
                row.get("codeGroup") + ":" + row.get("code"),
                null, String.valueOf(row), OmBodySupport.stringValue(body, "changeReason"));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "공통코드 저장");
        result.put("saved", true);
        result.put("codeGroup", row.get("codeGroup"));
        result.put("code", row.get("code"));
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
