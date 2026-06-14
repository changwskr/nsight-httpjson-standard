package com.nh.nsight.marketing.common.web.security;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.config.BusinessModuleProperties;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationValidator {
    private final BusinessModuleProperties properties;

    public AuthorizationValidator(BusinessModuleProperties properties) {
        this.properties = properties;
    }

    public void validate(TransactionContext context) {
        if (!properties.isAuthorizationValidationEnabled()) {
            return;
        }
        // TODO: serviceId / transactionCode 기준 메뉴권한, 기능권한, 데이터권한을 검증한다.
    }
}
