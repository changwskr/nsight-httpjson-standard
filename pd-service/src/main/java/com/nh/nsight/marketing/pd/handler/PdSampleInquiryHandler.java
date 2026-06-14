package com.nh.nsight.marketing.pd.handler;

import com.nh.nsight.marketing.pd.facade.PdSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class PdSampleInquiryHandler implements TransactionHandler {
    private final PdSampleFacade facade;

    public PdSampleInquiryHandler(PdSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "PD.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
