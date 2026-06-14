package com.nh.nsight.marketing.mg.handler;

import com.nh.nsight.marketing.mg.facade.MgSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MgSampleInquiryHandler implements TransactionHandler {
    private final MgSampleFacade facade;

    public MgSampleInquiryHandler(MgSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "MG.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
