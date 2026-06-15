package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmErrorCodeFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmErrorCodeSaveHandler implements TransactionHandler {
    private final OmErrorCodeFacade facade;

    public OmErrorCodeSaveHandler(OmErrorCodeFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.ErrorCode.save";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.save(context, body);
    }
}
