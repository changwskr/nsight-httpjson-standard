package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmCommonCodeFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmCommonCodeInquiryHandler implements TransactionHandler {
    private final OmCommonCodeFacade facade;

    public OmCommonCodeInquiryHandler(OmCommonCodeFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.CommonCode.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
