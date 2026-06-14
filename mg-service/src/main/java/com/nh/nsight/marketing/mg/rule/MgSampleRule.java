package com.nh.nsight.marketing.mg.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MgSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"MG".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-MG-BIZ-0001", "업무코드가 MG가 아닙니다.");
        }
    }
}
