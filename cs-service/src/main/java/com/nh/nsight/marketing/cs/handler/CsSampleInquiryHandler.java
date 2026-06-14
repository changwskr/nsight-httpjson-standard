package com.nh.nsight.marketing.cs.handler;

import com.nh.nsight.marketing.cs.facade.CsSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CsSampleInquiryHandler implements TransactionHandler {
    private final CsSampleFacade facade;

    public CsSampleInquiryHandler(CsSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "CS.Sample.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
