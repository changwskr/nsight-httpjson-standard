package com.nh.nsight.marketing.om.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.service.OmBatchService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OmBatchFacade {
    private final OmBatchService service;

    public OmBatchFacade(OmBatchService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 10)
    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        return service.inquiry(context, body);
    }

    @Transactional(timeout = 30)
    public Map<String, Object> execute(TransactionContext context, Map<String, Object> body) {
        return service.execute(context, body);
    }
}
