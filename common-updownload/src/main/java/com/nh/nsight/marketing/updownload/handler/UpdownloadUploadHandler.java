package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadUploadFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadUploadHandler implements TransactionHandler {
    private final UpdownloadUploadFacade facade;

    public UpdownloadUploadHandler(UpdownloadUploadFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.upload";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.upload(context, body);
    }
}
