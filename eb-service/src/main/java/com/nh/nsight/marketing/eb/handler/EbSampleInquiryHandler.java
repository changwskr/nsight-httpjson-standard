package com.nh.nsight.marketing.eb.handler;

import com.nh.nsight.marketing.eb.facade.EbSampleFacade;
import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class EbSampleInquiryHandler implements TransactionHandler {
    private final EbSampleFacade facade;

    public EbSampleInquiryHandler(EbSampleFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "EB.Sample.inquiry";
    }

    @Override
    public Object handle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
