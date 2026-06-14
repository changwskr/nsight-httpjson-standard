package com.nh.nsight.marketing.pc.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PcSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"PC".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-PC-BIZ-0001", "업무코드가 PC가 아닙니다.");
        }
    }
}
