package com.nh.nsight.marketing.ss.service;

import com.nh.nsight.marketing.ss.dao.SsSampleDao;
import com.nh.nsight.marketing.ss.rule.SsSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SsSampleService {
    private final SsSampleRule rule;
    private final SsSampleDao dao;

    public SsSampleService(SsSampleRule rule, SsSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "SS");
        result.put("businessName", "Sales Support");
        result.put("businessGroup", "지원");
        result.put("description", "영업활동 지원 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
