package com.nh.nsight.marketing.ep.handler;

import com.nh.nsight.marketing.ep.facade.EpSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EpSampleInquiryHandler implements TransactionHandler {
    private final EpSampleFacade facade;

    public EpSampleInquiryHandler(EpSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "EP.Sample.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
