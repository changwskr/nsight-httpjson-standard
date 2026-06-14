package com.nh.nsight.marketing.sv.handler;

import com.nh.nsight.marketing.sv.facade.SvSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class SvSampleInquiryHandler implements TransactionHandler {
    private final SvSampleFacade facade;

    public SvSampleInquiryHandler(SvSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "SV.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
