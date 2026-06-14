package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.om.facade.OmSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmSampleInquiryHandler implements TransactionHandler {
    private final OmSampleFacade facade;

    public OmSampleInquiryHandler(OmSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
