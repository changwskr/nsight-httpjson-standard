package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.common.web.audit.AuditLogService;
import com.nh.nsight.marketing.common.web.log.TransactionLogService;
import com.nh.nsight.marketing.common.web.monitoring.MetricsPublisher;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class PostProcessor {
    private final TransactionLogService transactionLogService;
    private final AuditLogService auditLogService;
    private final MetricsPublisher metricsPublisher;

    public PostProcessor(TransactionLogService transactionLogService, AuditLogService auditLogService, MetricsPublisher metricsPublisher) {
        this.transactionLogService = transactionLogService;
        this.auditLogService = auditLogService;
        this.metricsPublisher = metricsPublisher;
    }

    public StandardResponse<Object> success(TransactionContext context, Object body) {
        Result result = Result.success(elapsed(context));
        return build(context, result, body);
    }

    public StandardResponse<Object> businessFail(TransactionContext context, BusinessException e) {
        Result result = Result.fail(e.getErrorCode(), e.getMessage(), elapsed(context));
        return build(context, result, null);
    }

    public StandardResponse<Object> systemError(TransactionContext context, Exception e) {
        Result result = Result.error("E-COM-SYS-9999", "시스템 처리 중 오류가 발생했습니다.", elapsed(context));
        return build(context, result, null);
    }

    private StandardResponse<Object> build(TransactionContext context, Result result, Object body) {
        StandardHeader responseHeader = context.getHeader().copy();
        responseHeader.setResponseTime(DateTimeUtil.nowKst());
        transactionLogService.end(context, result);
        auditLogService.auditIfRequired(context);
        metricsPublisher.publish(context, result);
        MDC.clear();
        return StandardResponse.of(responseHeader, result, body);
    }

    private long elapsed(TransactionContext context) {
        return Duration.between(context.getStartTime(), Instant.now()).toMillis();
    }
}
