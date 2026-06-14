package com.nh.nsight.marketing.bp.service;

import com.nh.nsight.marketing.bp.dao.BpSampleDao;
import com.nh.nsight.marketing.bp.rule.BpSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BpSampleService {
    private final BpSampleRule rule;
    private final BpSampleDao dao;

    public BpSampleService(BpSampleRule rule, BpSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "BP");
        result.put("businessName", "Behavior Processing");
        result.put("businessGroup", "실시간");
        result.put("description", "고객 행동정보 처리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
