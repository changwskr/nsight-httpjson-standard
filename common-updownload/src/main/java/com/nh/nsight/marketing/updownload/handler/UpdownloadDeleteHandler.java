package com.nh.nsight.marketing.updownload.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.updownload.facade.UpdownloadDeleteFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UpdownloadDeleteHandler implements TransactionHandler {
    private final UpdownloadDeleteFacade facade;

    public UpdownloadDeleteHandler(UpdownloadDeleteFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "UD.File.delete";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.delete(context, body);
    }
}
