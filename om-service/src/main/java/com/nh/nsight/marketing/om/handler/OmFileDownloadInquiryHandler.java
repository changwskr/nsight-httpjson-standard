package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmFileDownloadFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmFileDownloadInquiryHandler implements TransactionHandler {
    private final OmFileDownloadFacade facade;

    public OmFileDownloadInquiryHandler(OmFileDownloadFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.FileDownload.inquiry";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.inquiry(context, body);
    }
}
