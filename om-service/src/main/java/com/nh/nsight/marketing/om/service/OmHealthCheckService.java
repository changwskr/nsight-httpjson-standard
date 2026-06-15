package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.om.dao.OmOperationDao;
import com.nh.nsight.marketing.om.rule.OmOperationRule;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmHealthCheckService {
    private final OmOperationRule rule;
    private final OmOperationDao dao;

    public OmHealthCheckService(OmOperationRule rule, OmOperationDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateOperation(context);
        List<Map<String, Object>> apStatus = dao.selectApStatus();
        List<Map<String, Object>> dbStatus = dao.selectDbStatus();
        List<Map<String, Object>> deployStatus = dao.selectDeployStatus();

        String overall = resolveOverall(apStatus, dbStatus, deployStatus);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("screen", "Health Check / 배포 조회");
        result.put("overallStatus", overall);
        result.put("checkedAt", DateTimeUtil.nowKst());
        result.put("apStatus", apStatus);
        result.put("dbStatus", dbStatus);
        result.put("deployStatus", deployStatus);
        result.put("activeSessionCount", dao.selectActiveSessionCount());
        return result;
    }

    private String resolveOverall(List<Map<String, Object>> ap, List<Map<String, Object>> db,
                                  List<Map<String, Object>> deploy) {
        boolean warn = containsStatus(ap, "WARN") || containsStatus(db, "WARN");
        boolean fail = containsStatus(ap, "FAIL", "DOWN", "ERROR") || containsStatus(db, "FAIL", "DOWN", "ERROR")
                || containsStatus(deploy, "DOWN", "FAIL");
        if (fail) {
            return "CRITICAL";
        }
        if (warn) {
            return "WARN";
        }
        return "NORMAL";
    }

    private boolean containsStatus(List<Map<String, Object>> rows, String... statuses) {
        for (Map<String, Object> row : rows) {
            Object value = row.get("healthStatus");
            if (value == null) {
                continue;
            }
            String status = String.valueOf(value).toUpperCase();
            for (String s : statuses) {
                if (status.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }
}
