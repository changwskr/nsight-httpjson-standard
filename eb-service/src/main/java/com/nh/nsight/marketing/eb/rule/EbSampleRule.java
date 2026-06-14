package com.nh.nsight.marketing.eb.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EbSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"EB".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-EB-BIZ-0001", "업무코드가 EB가 아닙니다.");
        }
    }
}
