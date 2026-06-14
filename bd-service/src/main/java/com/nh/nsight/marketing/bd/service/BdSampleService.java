package com.nh.nsight.marketing.bd.service;

import com.nh.nsight.marketing.bd.dao.BdSampleDao;
import com.nh.nsight.marketing.bd.rule.BdSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BdSampleService {
    private final BdSampleRule rule;
    private final BdSampleDao dao;

    public BdSampleService(BdSampleRule rule, BdSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "BD");
        result.put("businessName", "Behavior Data");
        result.put("businessGroup", "데이터");
        result.put("description", "고객 행동 데이터 관리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
