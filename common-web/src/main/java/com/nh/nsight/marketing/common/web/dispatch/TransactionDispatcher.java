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
        for (TransactionHandler handler : handlers) {
            handlerMap.put(handler.getServiceId(), handler);
        }
    }

    public Object dispatch(TransactionContext context, Map<String, Object> body) {
        TransactionHandler handler = handlerMap.get(context.getServiceId());
        if (handler == null) {
            throw new BusinessException("E-COM-HDL-0001", "등록되지 않은 서비스ID입니다: " + context.getServiceId());
        }
        return handler.handle(context, body);
    }
}
