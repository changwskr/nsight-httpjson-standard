package com.nh.nsight.marketing.sv.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SvSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★ [SvSampleRule.validateInquiry] start");
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        if (!"SV".equalsIgnoreCase(context.getBusinessCode())) {
            System.out.println("validationFailed: businessCode is not SV");
            System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★[SvSampleRule.validateInquiry] end");
            throw new BusinessException("E-SV-BIZ-0001", "업무코드가 SV가 아닙니다.");
        }
        System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★ [SvSampleRule.validateInquiry] end");
    }
}
