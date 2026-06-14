package com.nh.nsight.marketing.bc.handler;

import com.nh.nsight.marketing.bc.facade.BcSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BcSampleInquiryHandler implements TransactionHandler {
    private final BcSampleFacade facade;

    public BcSampleInquiryHandler(BcSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "BC.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
