package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadUpdateFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadUpdateHandler implements TransactionHandler {
    private final UpdownloadUpdateFacade facade;

    public UpdownloadUpdateHandler(UpdownloadUpdateFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.update";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.update(context, body);
    }
}
