package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmMenuFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmMenuInquiryHandler implements TransactionHandler {
    private final OmMenuFacade facade;

    public OmMenuInquiryHandler(OmMenuFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Menu.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
