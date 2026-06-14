package com.nh.nsight.marketing.cc.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CcSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"CC".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-CC-BIZ-0001", "업무코드가 CC가 아닙니다.");
        }
    }
}
