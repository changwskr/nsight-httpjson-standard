package com.nh.nsight.marketing.bd.handler;

import com.nh.nsight.marketing.bd.facade.BdSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class BdSampleInquiryHandler implements TransactionHandler {
    private final BdSampleFacade facade;

    public BdSampleInquiryHandler(BdSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "BD.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
