package com.nh.nsight.marketing.common.web.dispatch;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TransactionDispatcher {
    private final Map<String, TransactionHandler> handlerMap = new HashMap<>();

    public TransactionDispatcher(List<TransactionHandler> handlers) {
        System.out.println("====================================================================[TransactionDispatcher.<init>] start");
        System.out.println("handlerCount: " + handlers.size());
        for (TransactionHandler handler : handlers) {
            handlerMap.put(handler.getServiceId(), handler);
            System.out.println("registeredServiceId: " + handler.getServiceId() + ", handler: " + handler.getClass().getSimpleName());
        }
        System.out.println("====================================================================[TransactionDispatcher.<init>] end");
    }

    public Object dispatch(TransactionContext context, Map<String, Object> body) {
        System.out.println("====================================================================[TransactionDispatcher.dispatch] start");
        System.out.println("serviceId: " + context.getServiceId());
        System.out.println("context: " + context);
        System.out.println("body: " + body);
        TransactionHandler handler = handlerMap.get(context.getServiceId());
        if (handler == null) {
            System.out.println("handler: not found");
            System.out.println("====================================================================[TransactionDispatcher.dispatch] end");
            throw new BusinessException("E-COM-HDL-0001", "등록되지 않은 서비스ID입니다: " + context.getServiceId());
        }
        System.out.println("handler: " + handler.getClass().getSimpleName());
        Object result = handler.handle(context, body);
        System.out.println("result: " + result);
        System.out.println("====================================================================[TransactionDispatcher.dispatch] end");
        return result;
    }
}
