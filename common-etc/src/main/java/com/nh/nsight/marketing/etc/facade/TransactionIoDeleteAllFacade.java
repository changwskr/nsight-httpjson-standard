package com.nh.nsight.marketing.etc.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.etc.service.TransactionIoService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionIoDeleteAllFacade {
    private final TransactionIoService service;

    public TransactionIoDeleteAllFacade(TransactionIoService service) {
        this.service = service;
    }

    @Transactional(timeout = 10)
    public Map<String, Object> deleteAll(TransactionContext context, Map<String, Object> body) {
        return service.deleteAll(context, body);
    }
}
