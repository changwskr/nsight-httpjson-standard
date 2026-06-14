package com.nh.nsight.marketing.pd.service;

import com.nh.nsight.marketing.pd.dao.PdSampleDao;
import com.nh.nsight.marketing.pd.rule.PdSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PdSampleService {
    private final PdSampleRule rule;
    private final PdSampleDao dao;

    public PdSampleService(PdSampleRule rule, PdSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "PD");
        result.put("businessName", "Product");
        result.put("businessGroup", "마케팅");
        result.put("description", "상품정보 관리 및 활용 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
