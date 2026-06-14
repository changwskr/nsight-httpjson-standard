package com.nh.nsight.marketing.etc.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.etc.facade.TransactionIoDetailFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TransactionIoDetailHandler implements TransactionHandler {
    private final TransactionIoDetailFacade facade;

    public TransactionIoDetailHandler(TransactionIoDetailFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "ET.TransactionIo.detail";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.detail(context, body);
    }
}
