package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmDataAuthFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmDataAuthInquiryHandler implements TransactionHandler {
    private final OmDataAuthFacade facade;

    public OmDataAuthInquiryHandler(OmDataAuthFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.DataAuth.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
