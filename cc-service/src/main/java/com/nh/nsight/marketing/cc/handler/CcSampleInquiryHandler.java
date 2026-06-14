package com.nh.nsight.marketing.cc.handler;

import com.nh.nsight.marketing.cc.facade.CcSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CcSampleInquiryHandler implements TransactionHandler {
    private final CcSampleFacade facade;

    public CcSampleInquiryHandler(CcSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "CC.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
