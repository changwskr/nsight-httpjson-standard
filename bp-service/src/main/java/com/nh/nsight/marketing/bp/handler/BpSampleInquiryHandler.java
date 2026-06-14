package com.nh.nsight.marketing.bp.handler;

import com.nh.nsight.marketing.bp.facade.BpSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BpSampleInquiryHandler implements TransactionHandler {
    private final BpSampleFacade facade;

    public BpSampleInquiryHandler(BpSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "BP.Sample.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
