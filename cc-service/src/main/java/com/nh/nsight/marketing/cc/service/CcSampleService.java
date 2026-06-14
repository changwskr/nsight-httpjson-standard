package com.nh.nsight.marketing.cc.service;

import com.nh.nsight.marketing.cc.dao.CcSampleDao;
import com.nh.nsight.marketing.cc.rule.CcSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CcSampleService {
    private final CcSampleRule rule;
    private final CcSampleDao dao;

    public CcSampleService(CcSampleRule rule, CcSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "CC");
        result.put("businessName", "Common");
        result.put("businessGroup", "공통");
        result.put("description", "마케팅 플랫폼 공통 기능 및 공통 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
