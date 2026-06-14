package com.nh.nsight.marketing.etc.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TransactionIoRule {
    public void validateBusinessCode(TransactionContext context) {
        if (!"ET".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-ET-BIZ-0001", "업무코드가 ET가 아닙니다.");
        }
    }

    public void validateList(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
    }

    public void validateDetail(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
        if (!StringUtils.hasText(stringValue(body.get("logId")))
                && !StringUtils.hasText(stringValue(body.get("guid")))) {
            throw new BusinessException("E-ET-IO-0001", "logId 또는 guid는 필수입니다.");
        }
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
