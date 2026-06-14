package com.nh.nsight.marketing.mg.service;

import com.nh.nsight.marketing.mg.dao.MgSampleDao;
import com.nh.nsight.marketing.mg.rule.MgSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MgSampleService {
    private final MgSampleRule rule;
    private final MgSampleDao dao;

    public MgSampleService(MgSampleRule rule, MgSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "MG");
        result.put("businessName", "Message");
        result.put("businessGroup", "지원");
        result.put("description", "메시지 생성/발송/관리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
