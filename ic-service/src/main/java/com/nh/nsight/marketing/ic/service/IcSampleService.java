package com.nh.nsight.marketing.ic.service;

import com.nh.nsight.marketing.ic.dao.IcSampleDao;
import com.nh.nsight.marketing.ic.rule.IcSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class IcSampleService {
    private final IcSampleRule rule;
    private final IcSampleDao dao;

    public IcSampleService(IcSampleRule rule, IcSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "IC");
        result.put("businessName", "Integration Customer");
        result.put("businessGroup", "고객");
        result.put("description", "통합고객 기준정보 및 고객통합 관리");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
