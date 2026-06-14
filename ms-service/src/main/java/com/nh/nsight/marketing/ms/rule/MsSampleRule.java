package com.nh.nsight.marketing.ms.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MsSampleRule {
    public void validateInquiry(TransactionContext context, Map<String, Object> body) {
        if (!"MS".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-MS-BIZ-0001", "업무코드가 MS가 아닙니다.");
        }
    }
}
