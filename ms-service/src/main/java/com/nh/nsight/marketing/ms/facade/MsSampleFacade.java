package com.nh.nsight.marketing.ms.facade;

import com.nh.nsight.marketing.ms.service.MsSampleService;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MsSampleFacade {
    private final MsSampleService service;

    public MsSampleFacade(MsSampleService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        return service.inquiry(context, body);
    }
}
