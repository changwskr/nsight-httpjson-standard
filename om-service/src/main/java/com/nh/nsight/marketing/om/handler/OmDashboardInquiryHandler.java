package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmDashboardFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmDashboardInquiryHandler implements TransactionHandler {
    private final OmDashboardFacade facade;

    public OmDashboardInquiryHandler(OmDashboardFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Dashboard.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
