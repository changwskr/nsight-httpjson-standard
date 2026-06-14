package com.nh.nsight.marketing.bc.service;

import com.nh.nsight.marketing.bc.dao.BcSampleDao;
import com.nh.nsight.marketing.bc.rule.BcSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class BcSampleService {
    private final BcSampleRule rule;
    private final BcSampleDao dao;

    public BcSampleService(BcSampleRule rule, BcSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "BC");
        result.put("businessName", "Business Customer");
        result.put("businessGroup", "고객");
        result.put("description", "기업고객 대상 처리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
