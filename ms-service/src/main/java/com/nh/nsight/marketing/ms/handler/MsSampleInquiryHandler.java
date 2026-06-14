package com.nh.nsight.marketing.ms.handler;

import com.nh.nsight.marketing.ms.facade.MsSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MsSampleInquiryHandler implements TransactionHandler {
    private final MsSampleFacade facade;

    public MsSampleInquiryHandler(MsSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "MS.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
