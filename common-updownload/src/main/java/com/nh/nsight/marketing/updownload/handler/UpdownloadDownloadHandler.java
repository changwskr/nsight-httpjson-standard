package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadDownloadFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadDownloadHandler implements TransactionHandler {
    private final UpdownloadDownloadFacade facade;

    public UpdownloadDownloadHandler(UpdownloadDownloadFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.download";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.download(context, body);
    }
}
