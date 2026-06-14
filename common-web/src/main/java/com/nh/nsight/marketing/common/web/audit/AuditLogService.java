package com.nh.nsight.marketing.common.web.audit;

import com.nh.nsight.marketing.common.context.TransactionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditLogService {
    private static final Logger log = LoggerFactory.getLogger("AUDIT_LOG");

    public void auditIfRequired(TransactionContext context) {
        System.out.println("====================================================================[AuditLogService.auditIfRequired] start");
        System.out.println("context: " + context);
        System.out.println("serviceId: " + context.getServiceId());
        if (context.getServiceId() != null && (context.getServiceId().contains("Customer") || context.getServiceId().contains("download"))) {
            System.out.println("auditRequired: true");
            log.info("AUDIT guid={} serviceId={} userId={} branchId={} channelId={}",
                    context.getGuid(), context.getServiceId(), context.getHeader().getUserId(),
                    context.getHeader().getBranchId(), context.getHeader().getChannelId());
        } else {
            System.out.println("auditRequired: false");
        }
        System.out.println("====================================================================[AuditLogService.auditIfRequired] end");
    }
}
