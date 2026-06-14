package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.common.web.audit.AuditLogService;
import com.nh.nsight.marketing.common.web.etc.TransactionIoRecordPublisher;
import com.nh.nsight.marketing.common.web.log.TransactionLogService;
import com.nh.nsight.marketing.common.web.monitoring.MetricsPublisher;
import java.time.Duration;
import java.time.Instant;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class ETF {
    private final TransactionLogService transactionLogService;
    private final AuditLogService auditLogService;
    private final MetricsPublisher metricsPublisher;
    private final TransactionIoRecordPublisher transactionIoRecordPublisher;

    public ETF(TransactionLogService transactionLogService, AuditLogService auditLogService,
               MetricsPublisher metricsPublisher, TransactionIoRecordPublisher transactionIoRecordPublisher) {
        this.transactionLogService = transactionLogService;
        this.auditLogService = auditLogService;
        this.metricsPublisher = metricsPublisher;
        this.transactionIoRecordPublisher = transactionIoRecordPublisher;
    }

    public StandardResponse<Object> success(TransactionContext context, Object body) {
        System.out.println("====================================================================[ETF.success] start");
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        Result result = Result.success(elapsed(context));
        StandardResponse<Object> response = build(context, result, body);
        System.out.println("response: " + response);
        System.out.println("====================================================================[ETF.success] end");
        return response;
    }

    public StandardResponse<Object> businessFail(TransactionContext context, BusinessException e) {
        System.out.println("====================================================================[ETF.businessFail] start");
        System.out.println("context: " + context);
        System.out.println("errorCode: " + e.getErrorCode());
        System.out.println("message: " + e.getMessage());
        Result result = Result.fail(e.getErrorCode(), e.getMessage(), elapsed(context));
        StandardResponse<Object> response = build(context, result, null);
        System.out.println("response: " + response);
        System.out.println("====================================================================[ETF.businessFail] end");
        return response;
    }

    public StandardResponse<Object> systemError(TransactionContext context, Exception e) {
        System.out.println("====================================================================[ETF.systemError] start");
        System.out.println("context: " + context);
        System.out.println("exception: " + e);
        Result result = Result.error("E-COM-SYS-9999", "시스템 처리 중 오류가 발생했습니다.", elapsed(context));
        StandardResponse<Object> response = build(context, result, null);
        System.out.println("response: " + response);
        System.out.println("====================================================================[ETF.systemError] end");
        return response;
    }

    private StandardResponse<Object> build(TransactionContext context, Result result, Object body) {
        System.out.println("====================================================================[ETF.build] start");
        System.out.println("context: " + context);
        System.out.println("result: " + result);
        System.out.println("body: " + body);
        applyEndHeader(context.getHeader());
        StandardHeader responseHeader = context.getHeader().copy();
        transactionLogService.end(context, result);
        transactionIoRecordPublisher.publish(context, responseHeader, result);
        auditLogService.auditIfRequired(context);
        metricsPublisher.publish(context, result);
        MDC.clear();
        StandardResponse<Object> response = StandardResponse.of(responseHeader, result, body);
        System.out.println("response: " + response);
        System.out.println("====================================================================[ETF.build] end");
        return response;
    }

    private long elapsed(TransactionContext context) {
        return Duration.between(context.getStartTime(), Instant.now()).toMillis();
    }

    private void applyEndHeader(StandardHeader header) {
        String outTime = DateTimeUtil.nowKst();
        header.setTransactionOuttime(outTime);
        header.setResponseTime(outTime);
        if (isBlank(header.getSystemDate())) {
            header.setSystemDate(DateTimeUtil.todayKst());
        }
        if (isBlank(header.getBizDate())) {
            header.setBizDate(header.getSystemDate());
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
