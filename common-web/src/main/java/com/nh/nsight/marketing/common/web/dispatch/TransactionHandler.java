package com.nh.nsight.marketing.common.web.dispatch;

import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.Map;

public interface TransactionHandler {
    String getServiceId();
    Object handle(TransactionContext context, Map<String, Object> body);
}
