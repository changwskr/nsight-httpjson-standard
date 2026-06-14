package com.nh.nsight.marketing.ep.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EpSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"EP".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-EP-BIZ-0001", "업무코드가 EP가 아닙니다.");
        }
    }
}
