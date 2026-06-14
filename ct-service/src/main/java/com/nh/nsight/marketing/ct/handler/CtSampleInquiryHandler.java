package com.nh.nsight.marketing.ct.handler;

import com.nh.nsight.marketing.ct.facade.CtSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CtSampleInquiryHandler implements TransactionHandler {
    private final CtSampleFacade facade;

    public CtSampleInquiryHandler(CtSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "CT.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
