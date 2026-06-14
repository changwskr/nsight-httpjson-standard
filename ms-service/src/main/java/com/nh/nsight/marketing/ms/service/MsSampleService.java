package com.nh.nsight.marketing.ms.service;

import com.nh.nsight.marketing.ms.dao.MsSampleDao;
import com.nh.nsight.marketing.ms.rule.MsSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class MsSampleService {
    private final MsSampleRule rule;
    private final MsSampleDao dao;

    public MsSampleService(MsSampleRule rule, MsSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "MS");
        result.put("businessName", "Mini Single View");
        result.put("businessGroup", "고객");
        result.put("description", "고객 단위 조회/요약 제공 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
