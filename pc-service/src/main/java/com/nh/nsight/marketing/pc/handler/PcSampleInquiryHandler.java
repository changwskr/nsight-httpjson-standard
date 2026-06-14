package com.nh.nsight.marketing.pc.handler;

import com.nh.nsight.marketing.pc.facade.PcSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PcSampleInquiryHandler implements TransactionHandler {
    private final PcSampleFacade facade;

    public PcSampleInquiryHandler(PcSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "PC.Sample.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
