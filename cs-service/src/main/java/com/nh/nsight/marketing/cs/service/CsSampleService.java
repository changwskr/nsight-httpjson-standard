package com.nh.nsight.marketing.cs.service;

import com.nh.nsight.marketing.cs.dao.CsSampleDao;
import com.nh.nsight.marketing.cs.rule.CsSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CsSampleService {
    private final CsSampleRule rule;
    private final CsSampleDao dao;

    public CsSampleService(CsSampleRule rule, CsSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "CS");
        result.put("businessName", "Common Service");
        result.put("businessGroup", "지원");
        result.put("description", "공통 서비스 운영 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
