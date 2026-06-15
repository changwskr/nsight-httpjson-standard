package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmCommonCodeFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmCommonCodeSaveHandler implements TransactionHandler {
    private final OmCommonCodeFacade facade;

    public OmCommonCodeSaveHandler(OmCommonCodeFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.CommonCode.save";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.save(context, body);
    }
}
