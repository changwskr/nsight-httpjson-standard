package com.nh.nsight.marketing.eb.facade;

import com.nh.nsight.marketing.eb.service.EbSampleService;
import com.nh.nsight.marketing.common.context.TransactionContext;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EbSampleFacade {
    private final EbSampleService service;

    public EbSampleFacade(EbSampleService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        return service.inquiry(context, body);
    }
}
