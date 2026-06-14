package com.nh.nsight.marketing.etc.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.etc.facade.TransactionIoDeleteAllFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TransactionIoDeleteAllHandler implements TransactionHandler {
    private final TransactionIoDeleteAllFacade facade;

    public TransactionIoDeleteAllHandler(TransactionIoDeleteAllFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "ET.TransactionIo.deleteAll";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.deleteAll(context, body);
    }
}
