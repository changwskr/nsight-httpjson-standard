package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.web.duplicate.IdempotencyChecker;
import com.nh.nsight.marketing.common.web.log.TransactionLogService;
import com.nh.nsight.marketing.common.web.security.AuthorizationValidator;
import com.nh.nsight.marketing.common.web.security.SessionValidator;
import com.nh.nsight.marketing.common.web.validation.StandardHeaderValidator;
import java.time.Instant;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class STF {
    private final StandardHeaderValidator headerValidator;
    private final SessionValidator sessionValidator;
    private final AuthorizationValidator authorizationValidator;
    private final IdempotencyChecker idempotencyChecker;
    private final TransactionLogService transactionLogService;

    public STF(StandardHeaderValidator headerValidator, SessionValidator sessionValidator,
                        AuthorizationValidator authorizationValidator, IdempotencyChecker idempotencyChecker,
                        TransactionLogService transactionLogService) {
        this.headerValidator = headerValidator;
        this.sessionValidator = sessionValidator;
        this.authorizationValidator = authorizationValidator;
        this.idempotencyChecker = idempotencyChecker;
        this.transactionLogService = transactionLogService;
    }

    public TransactionContext preProcess(String pathBusinessCode, StandardRequest<Map<String, Object>> request) {
        System.out.println("====================================================================[STF.preProcess] start");
        System.out.println("pathBusinessCode: " + pathBusinessCode);
        System.out.println("request: " + request);
        headerValidator.validateAndNormalize(request.getHeader(), pathBusinessCode == null ? null : pathBusinessCode.toUpperCase());
        TransactionContext context = new TransactionContext(request.getHeader(), Instant.now());
        context.setPathBusinessCode(pathBusinessCode);
        MDC.put("guid", context.getGuid());
        MDC.put("traceId", context.getTraceId());
        MDC.put("userId", context.getHeader().getUserId());
        MDC.put("branchId", context.getHeader().getBranchId());
        MDC.put("serviceId", context.getServiceId());
        sessionValidator.validate(context);
        authorizationValidator.validate(context);
        idempotencyChecker.check(context);
        transactionLogService.start(context);
        System.out.println("context: " + context);
        System.out.println("====================================================================[STF.preProcess] end");
        return context;
    }
}
