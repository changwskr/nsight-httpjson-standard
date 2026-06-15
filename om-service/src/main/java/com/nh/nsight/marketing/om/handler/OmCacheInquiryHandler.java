package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmCacheFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmCacheInquiryHandler implements TransactionHandler {
    private final OmCacheFacade facade;

    public OmCacheInquiryHandler(OmCacheFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Cache.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
