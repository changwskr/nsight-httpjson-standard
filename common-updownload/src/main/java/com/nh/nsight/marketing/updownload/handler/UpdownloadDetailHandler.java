package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadDetailFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadDetailHandler implements TransactionHandler {
    private final UpdownloadDetailFacade facade;

    public UpdownloadDetailHandler(UpdownloadDetailFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.detail";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.detail(context, body);
    }
}
