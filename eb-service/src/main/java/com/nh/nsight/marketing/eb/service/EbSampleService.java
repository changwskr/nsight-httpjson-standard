package com.nh.nsight.marketing.eb.service;

import com.nh.nsight.marketing.eb.dao.EbSampleDao;
import com.nh.nsight.marketing.eb.rule.EbSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class EbSampleService {
    private final EbSampleRule rule;
    private final EbSampleDao dao;

    public EbSampleService(EbSampleRule rule, EbSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "EB");
        result.put("businessName", "EBM");
        result.put("businessGroup", "마케팅");
        result.put("description", "이벤트 기반 마케팅 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
