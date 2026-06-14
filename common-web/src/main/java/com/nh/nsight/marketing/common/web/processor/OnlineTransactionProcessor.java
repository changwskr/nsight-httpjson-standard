package com.nh.nsight.marketing.common.web.processor;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.web.dispatch.TransactionDispatcher;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OnlineTransactionProcessor {
    private final PreProcessor preProcessor;
    private final TransactionDispatcher transactionDispatcher;
    private final PostProcessor postProcessor;

    public OnlineTransactionProcessor(PreProcessor preProcessor, TransactionDispatcher transactionDispatcher, PostProcessor postProcessor) {
        this.preProcessor = preProcessor;
        this.transactionDispatcher = transactionDispatcher;
        this.postProcessor = postProcessor;
    }

    public StandardResponse<Object> process(String pathBusinessCode, StandardRequest<Map<String, Object>> request) {
        TransactionContext context = preProcessor.preProcess(pathBusinessCode, request);
        try {
            Object body = transactionDispatcher.dispatch(context, request.getBody());
            return postProcessor.success(context, body);
        } catch (BusinessException e) {
            return postProcessor.businessFail(context, e);
        } catch (Exception e) {
            return postProcessor.systemError(context, e);
        }
    }
}
