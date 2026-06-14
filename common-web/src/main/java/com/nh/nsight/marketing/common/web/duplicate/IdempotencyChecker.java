package com.nh.nsight.marketing.common.web.duplicate;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.ProcessingType;
import com.nh.nsight.marketing.common.web.config.BusinessModuleProperties;
import org.springframework.stereotype.Component;

@Component
public class IdempotencyChecker {
    private final BusinessModuleProperties properties;

    public IdempotencyChecker(BusinessModuleProperties properties) {
        this.properties = properties;
    }

    public void check(TransactionContext context) {
        if (!properties.isIdempotencyEnabled()) {
            return;
        }
        ProcessingType type = ProcessingType.valueOf(context.getHeader().getProcessingType());
        if (type == ProcessingType.CREATE || type == ProcessingType.UPDATE || type == ProcessingType.DELETE
                || type == ProcessingType.EXECUTE || type == ProcessingType.UPLOAD || type == ProcessingType.APPROVAL) {
            // TODO: TRANSACTION_STATE 테이블 또는 Redis에 GUID/IdempotencyKey 상태를 저장해 중복 요청을 차단한다.
        }
    }
}
