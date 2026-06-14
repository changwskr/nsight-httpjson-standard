package com.nh.nsight.marketing.ct.service;

import com.nh.nsight.marketing.ct.dao.CtSampleDao;
import com.nh.nsight.marketing.ct.rule.CtSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CtSampleService {
    private final CtSampleRule rule;
    private final CtSampleDao dao;

    public CtSampleService(CtSampleRule rule, CtSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "CT");
        result.put("businessName", "Contents");
        result.put("businessGroup", "지원");
        result.put("description", "마케팅 콘텐츠 관리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
