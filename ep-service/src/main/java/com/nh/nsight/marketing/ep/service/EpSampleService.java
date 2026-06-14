package com.nh.nsight.marketing.ep.service;

import com.nh.nsight.marketing.ep.dao.EpSampleDao;
import com.nh.nsight.marketing.ep.rule.EpSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EpSampleService {
    private final EpSampleRule rule;
    private final EpSampleDao dao;

    public EpSampleService(EpSampleRule rule, EpSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "EP");
        result.put("businessName", "Event Processing");
        result.put("businessGroup", "실시간");
        result.put("description", "이벤트 수집 및 실시간 처리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
