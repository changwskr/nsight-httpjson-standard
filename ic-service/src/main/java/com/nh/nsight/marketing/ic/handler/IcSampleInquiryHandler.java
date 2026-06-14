package com.nh.nsight.marketing.ic.handler;

import com.nh.nsight.marketing.ic.facade.IcSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class IcSampleInquiryHandler implements TransactionHandler {
    private final IcSampleFacade facade;

    public IcSampleInquiryHandler(IcSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "IC.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
