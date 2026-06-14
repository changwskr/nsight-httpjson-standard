package com.nh.nsight.marketing.ct.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CtSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"CT".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-CT-BIZ-0001", "업무코드가 CT가 아닙니다.");
        }
    }
}
