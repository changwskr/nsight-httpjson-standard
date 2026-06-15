package com.nh.nsight.marketing.om.handler;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.web.dispatch.TransactionHandler;
import com.nh.nsight.marketing.om.facade.OmCacheFacade;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OmCacheDeleteHandler implements TransactionHandler {
    private final OmCacheFacade facade;

    public OmCacheDeleteHandler(OmCacheFacade facade) {
        this.facade = facade;
    }

    @Override
    public String getServiceId() {
        return "OM.Cache.delete";
    }

    @Override
    public Object doHandle(TransactionContext context, Map<String, Object> body) {
        return facade.delete(context, body);
    }
}
