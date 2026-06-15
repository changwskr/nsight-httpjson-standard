package com.nh.nsight.marketing.om.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.service.OmCacheService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OmCacheFacade {
    private final OmCacheService service;

    public OmCacheFacade(OmCacheService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        return service.inquiry(context, body);
    }

    @Transactional(timeout = 10)
    public Map<String, Object> delete(TransactionContext context, Map<String, Object> body) {
        return service.delete(context, body);
    }
}
