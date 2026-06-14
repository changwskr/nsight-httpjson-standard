package com.nh.nsight.marketing.cm.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CmSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"CM".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-CM-BIZ-0001", "업무코드가 CM가 아닙니다.");
        }
    }
}
