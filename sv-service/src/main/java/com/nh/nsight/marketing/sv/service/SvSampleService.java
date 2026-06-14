package com.nh.nsight.marketing.sv.service;

import com.nh.nsight.marketing.sv.dao.SvSampleDao;
import com.nh.nsight.marketing.sv.rule.SvSampleRule;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SvSampleService {
    private final SvSampleRule rule;
    private final SvSampleDao dao;

    public SvSampleService(SvSampleRule rule, SvSampleDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        System.out.println(
                "□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□ [SvSampleService.inquiry] start");
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        rule.validateInquiry(context, body);
        Map<String, Object> data = dao.selectSample(context, body);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "SV");
        result.put("businessName", "Single View");
        result.put("businessGroup", "마케팅");
        result.put("description", "통합고객 기준의 고객 단위 조회 제공 업무");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("data", data);
        System.out.println(
                "□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□ [SvSampleService.inquiry] end");

        return result;
    }
}
