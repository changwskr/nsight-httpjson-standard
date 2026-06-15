package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import com.nh.nsight.marketing.om.support.OmBodySupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmDashboardService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmDashboardService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        String baseDate = OmBodySupport.stringValue(body, "baseDate");
        if (baseDate == null) {
            baseDate = DateTimeUtil.todayKst();
            if (baseDate.length() == 8) {
                baseDate = baseDate.substring(0, 4) + "-" + baseDate.substring(4, 6) + "-" + baseDate.substring(6, 8);
            }
        }

        Map<String, Object> txSummary = dao.selectTxSummary(baseDate);
        long total = toLong(txSummary.get("totalCount"));
        long errors = toLong(txSummary.get("errorCount"));
        double errorRate = total == 0 ? 0.0 : (errors * 100.0 / total);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "운영 대시보드");
        result.put("baseDate", baseDate);
        result.put("overallStatus", errors > 0 ? "WARN" : "NORMAL");
        result.put("errorRatePct", Math.round(errorRate * 10) / 10.0);
        result.put("timeoutCount", txSummary.get("timeoutCount"));
        result.put("avgElapsedMs", txSummary.get("avgElapsedMs"));
        result.put("apStatus", dao.selectApStatus());
        result.put("dbStatus", dao.selectDbStatus());
        result.put("deployStatus", dao.selectDeployStatus());
        result.put("activeSessionCount", dao.selectActiveSessionCount());
        result.put("errorTop", dao.selectErrorTop(baseDate));
        result.put("transactionSummary", txSummary);
        return result;
    }

    private long toLong(Object value) {
        if (value == null) {
            return 0L;
        }
        return Long.parseLong(String.valueOf(value));
    }
}
