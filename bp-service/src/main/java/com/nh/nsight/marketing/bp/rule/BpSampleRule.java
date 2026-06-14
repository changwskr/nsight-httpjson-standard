package com.nh.nsight.marketing.bp.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BpSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"BP".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-BP-BIZ-0001", "업무코드가 BP가 아닙니다.");
        }
    }
}
