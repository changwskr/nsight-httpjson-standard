package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmHealthCheckFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmHealthCheckInquiryHandler implements TransactionHandler {
    private final OmHealthCheckFacade facade;

    public OmHealthCheckInquiryHandler(OmHealthCheckFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.HealthCheck.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
