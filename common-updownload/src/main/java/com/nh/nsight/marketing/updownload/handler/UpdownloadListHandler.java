package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadListFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadListHandler implements TransactionHandler {
    private final UpdownloadListFacade facade;

    public UpdownloadListHandler(UpdownloadListFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.list";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.list(context, body);
    }
}
