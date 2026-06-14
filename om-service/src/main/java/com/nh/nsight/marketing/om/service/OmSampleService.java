package com.nh.nsight.marketing.om.service;

import com.nh.nsight.marketing.om.dao.OmSampleDao;
import com.nh.nsight.marketing.om.rule.OmSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class OmSampleService {
    private final OmSampleRule rule;
    private final OmSampleDao dao;

    public OmSampleService(OmSampleRule rule, OmSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "OM");
        result.put("businessName", "Operation Management");
        result.put("businessGroup", "운영");
        result.put("description", "운영관리, 관리자 기능, 기준정보/권한/메뉴/배치/감사 조회 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        return result;
    }
}
