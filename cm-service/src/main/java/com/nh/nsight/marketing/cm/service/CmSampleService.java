package com.nh.nsight.marketing.cm.service;

import com.nh.nsight.marketing.cm.dao.CmSampleDao;
import com.nh.nsight.marketing.cm.rule.CmSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class CmSampleService {
    private final CmSampleRule rule;
    private final CmSampleDao dao;

    public CmSampleService(CmSampleRule rule, CmSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "CM");
        result.put("businessName", "Campaign");
        result.put("businessGroup", "마케팅");
        result.put("description", "캠페인 실행 및 관리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
