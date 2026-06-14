package com.nh.nsight.marketing.bc.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BcSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"BC".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-BC-BIZ-0001", "업무코드가 BC가 아닙니다.");
        }
    }
}
