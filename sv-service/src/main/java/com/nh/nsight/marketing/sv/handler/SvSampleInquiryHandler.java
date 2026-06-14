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
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        System.out.println("====================================================================[SvSampleInquiryHandler.doHandle] start");
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        Object result = facade.inquiry(context, body);
        System.out.println("result: " + result);
        System.out.println("====================================================================[SvSampleInquiryHandler.doHandle] end");
        return result;
    }
}
