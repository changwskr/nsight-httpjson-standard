package com.nh.nsight.marketing.ss.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SsSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"SS".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-SS-BIZ-0001", "업무코드가 SS가 아닙니다.");
        }
    }
}
