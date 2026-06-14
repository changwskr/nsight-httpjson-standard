package com.nh.nsight.marketing.common.web.log;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogService {
    private static final Logger log = LoggerFactory.getLogger("TRANSACTION_LOG");

    public void start(TransactionContext context) {
        log.info("TX_START guid={} traceId={} businessCode={} serviceId={} transactionCode={} userId={} branchId={}",
                context.getGuid(), context.getTraceId(), context.getBusinessCode(), context.getServiceId(),
                context.getTransactionCode(), context.getHeader().getUserId(), context.getHeader().getBranchId());
    }

    public void end(TransactionContext context, Result result) {
        log.info("TX_END guid={} traceId={} businessCode={} serviceId={} transactionCode={} status={} resultCode={} errorCode={} elapsedMs={}",
                context.getGuid(), context.getTraceId(), context.getBusinessCode(), context.getServiceId(),
                context.getTransactionCode(), result.getStatus(), result.getResultCode(), result.getErrorCode(), result.getElapsedTimeMs());
    }
}
