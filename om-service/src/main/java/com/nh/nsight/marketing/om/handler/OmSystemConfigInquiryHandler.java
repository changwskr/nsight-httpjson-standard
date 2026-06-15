package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmSystemConfigFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmSystemConfigInquiryHandler implements TransactionHandler {
    private final OmSystemConfigFacade facade;

    public OmSystemConfigInquiryHandler(OmSystemConfigFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.SystemConfig.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
