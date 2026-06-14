package com.nh.nsight.marketing.cs.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CsSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"CS".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-CS-BIZ-0001", "업무코드가 CS가 아닙니다.");
        }
    }
}
