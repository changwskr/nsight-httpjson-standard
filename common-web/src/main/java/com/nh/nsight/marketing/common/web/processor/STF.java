package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.util.DateTimeUtil;
import com.nh.nsight.marketing.common.web.log.TransactionLogService;
import com.nh.nsight.marketing.common.web.validation.StandardHeaderValidator;
import java.time.Instant;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class STF {
    private final StandardHeaderValidator headerValidator;
    private final TransactionLogService transactionLogService;

    public STF(StandardHeaderValidator headerValidator, TransactionLogService transactionLogService) {
        this.headerValidator = headerValidator;
        this.transactionLogService = transactionLogService;
    }

    public TransactionContext preProcess(String pathBusinessCode, StandardRequest<Map<String, Object>> request) {
        System.out.println("====================================================================[STF.preProcess] start");
        System.out.println("pathBusinessCode: " + pathBusinessCode);
        System.out.println("request: " + request);
        headerValidator.validateAndNormalize(request.getHeader(), pathBusinessCode == null ? null : pathBusinessCode.toUpperCase());
        applyStartHeader(request.getHeader());
        TransactionContext context = new TransactionContext(
                request.getHeader(),
                request.getHeader().copy(),
                Instant.now());
        context.setPathBusinessCode(pathBusinessCode);
        MDC.put("guid", context.getGuid());
        MDC.put("traceId", context.getTraceId());
        MDC.put("userId", context.getHeader().getUserId());
        MDC.put("branchId", context.getHeader().getBranchId());
        MDC.put("serviceId", context.getServiceId());
        transactionLogService.start(context);
        System.out.println("context: " + context);
        System.out.println("====================================================================[STF.preProcess] end");
        return context;
    }

    private void applyStartHeader(StandardHeader header) {
        header.setBusinessCode(header.getBusinessCode().toUpperCase());
        String inTime = isBlank(header.getTransactionIntime()) ? DateTimeUtil.nowKst() : header.getTransactionIntime();
        header.setTransactionIntime(inTime);
        if (isBlank(header.getRequestTime())) {
            header.setRequestTime(inTime);
        }
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
