package com.nh.nsight.marketing.pc.service;

import com.nh.nsight.marketing.pc.dao.PcSampleDao;
import com.nh.nsight.marketing.pc.rule.PcSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class PcSampleService {
    private final PcSampleRule rule;
    private final PcSampleDao dao;

    public PcSampleService(PcSampleRule rule, PcSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "PC");
        result.put("businessName", "Private Customer");
        result.put("businessGroup", "고객");
        result.put("description", "개인고객 대상 처리 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
