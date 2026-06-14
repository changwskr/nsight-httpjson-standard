package com.nh.nsight.marketing.ss.handler;

import com.nh.nsight.marketing.ss.facade.SsSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SsSampleInquiryHandler implements TransactionHandler {
    private final SsSampleFacade facade;

    public SsSampleInquiryHandler(SsSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "SS.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
