package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmBatchFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmBatchExecuteHandler implements TransactionHandler {
    private final OmBatchFacade facade;

    public OmBatchExecuteHandler(OmBatchFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Batch.execute";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.execute(context, body);
    }
}
