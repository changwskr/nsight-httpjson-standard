package com.nh.nsight.marketing.common.web.security;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.web.config.BusinessModuleProperties;
import org.springframework.stereotype.Component;

@Component
public class SessionValidator {
    private final BusinessModuleProperties properties;

    public SessionValidator(BusinessModuleProperties properties) {
        this.properties = properties;
    }

    public void validate(TransactionContext context) {
        System.out.println("====================================================================[SessionValidator.validate] start");
        System.out.println("sessionValidationEnabled: " + properties.isSessionValidationEnabled());
        System.out.println("context: " + context);
        if (!properties.isSessionValidationEnabled()) {
            System.out.println("skipped: session validation disabled");
            System.out.println("====================================================================[SessionValidator.validate] end");
            return;
        }
        if (context.getHeader().getUserId() == null || context.getHeader().getUserId().isBlank()) {
            System.out.println("validationFailed: userId is blank");
            System.out.println("====================================================================[SessionValidator.validate] end");
            throw new BusinessException("E-COM-AUTH-0001", "세션 사용자 정보가 없습니다.");
        }
        System.out.println("====================================================================[SessionValidator.validate] end");
    }
}
