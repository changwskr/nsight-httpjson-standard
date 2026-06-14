package com.nh.nsight.marketing.cm.handler;

import com.nh.nsight.marketing.cm.facade.CmSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CmSampleInquiryHandler implements TransactionHandler {
    private final CmSampleFacade facade;

    public CmSampleInquiryHandler(CmSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "CM.Sample.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
