package com.nh.nsight.marketing.common.web.dispatch;

import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.Map;

public interface TransactionHandler {
    String getServiceId();

    default Object handle(TransactionContext context, Map<String, Object> body) {
        System.out.println("====================================================================[TransactionHandler.handle] start");
        System.out.println("serviceId: " + getServiceId());
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        Object result = doHandle(context, body);
        System.out.println("result: " + result);
        System.out.println("====================================================================[TransactionHandler.handle] end");
        return result;
    }

    Object doHandle(TransactionContext context, Map<String, Object> body);
}
