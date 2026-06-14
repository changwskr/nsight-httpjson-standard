package com.nh.nsight.marketing.pd.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PdSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"PD".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-PD-BIZ-0001", "업무코드가 PD가 아닙니다.");
        }
    }
}
