package com.nh.nsight.marketing.ic.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IcSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"IC".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-IC-BIZ-0001", "업무코드가 IC가 아닙니다.");
        }
    }
}
