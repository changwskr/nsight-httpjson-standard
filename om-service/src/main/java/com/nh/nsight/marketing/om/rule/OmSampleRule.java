package com.nh.nsight.marketing.om.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"OM".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-OM-BIZ-0001", "업무코드가 OM가 아닙니다.");
        }
    }
}
