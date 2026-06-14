package com.nh.nsight.marketing.etc.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.etc.service.TransactionIoService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionIoDetailFacade {
    private final TransactionIoService service;

    public TransactionIoDetailFacade(TransactionIoService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> detail(TransactionContext context, Map<String, Object> body) {
        return service.detail(context, body);
    }
}
