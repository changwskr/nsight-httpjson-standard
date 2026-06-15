package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmServiceCatalogFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmServiceCatalogInquiryHandler implements TransactionHandler {
    private final OmServiceCatalogFacade facade;

    public OmServiceCatalogInquiryHandler(OmServiceCatalogFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.ServiceCatalog.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
