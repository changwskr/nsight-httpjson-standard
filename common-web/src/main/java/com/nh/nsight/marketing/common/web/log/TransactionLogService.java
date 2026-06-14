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
        System.out.println("====================================================================[start] start");
        System.out.println("context: " + context);
        log.info("TX_START guid={} traceId={} systemId={} businessCode={} serviceId={} transactionCode={} transactionIntime={} systemDate={} bizDate={} userId={} branchId={}",
                context.getGuid(), context.getTraceId(), context.getHeader().getSystemId(), context.getBusinessCode(),
                context.getServiceId(), context.getTransactionCode(), context.getHeader().getTransactionIntime(),
                context.getHeader().getSystemDate(), context.getHeader().getBizDate(),
                context.getHeader().getUserId(), context.getHeader().getBranchId());
        System.out.println("====================================================================[start] end");
    }

    public void end(TransactionContext context, Result result) {
        System.out.println("====================================================================[end] start");
        System.out.println("context: " + context);
        System.out.println("result: " + result);
        log.info("TX_END guid={} traceId={} systemId={} businessCode={} serviceId={} transactionCode={} transactionIntime={} transactionOuttime={} systemDate={} bizDate={} status={} resultCode={} errorCode={} elapsedMs={}",
                context.getGuid(), context.getTraceId(), context.getHeader().getSystemId(), context.getBusinessCode(),
                context.getServiceId(), context.getTransactionCode(), context.getHeader().getTransactionIntime(),
                context.getHeader().getTransactionOuttime(), context.getHeader().getSystemDate(),
                context.getHeader().getBizDate(), result.getStatus(), result.getResultCode(), result.getErrorCode(),
                result.getElapsedTimeMs());
        System.out.println("====================================================================[end] end");
    }
}
