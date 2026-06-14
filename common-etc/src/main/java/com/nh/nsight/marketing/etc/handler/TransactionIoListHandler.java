package com.nh.nsight.marketing.etc.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.etc.facade.TransactionIoListFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TransactionIoListHandler implements TransactionHandler {
    private final TransactionIoListFacade facade;

    public TransactionIoListHandler(TransactionIoListFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "ET.TransactionIo.list";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.list(context, body);
    }
}
