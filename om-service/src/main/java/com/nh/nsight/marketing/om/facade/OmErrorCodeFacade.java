package com.nh.nsight.marketing.om.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.om.service.OmErrorCodeService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OmErrorCodeFacade {
    private final OmErrorCodeService service;

    public OmErrorCodeFacade(OmErrorCodeService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> inquiry(TransactionContext context, Map<String, Object> body) {
        return service.inquiry(context, body);
    }

    @Transactional(timeout = 5)
    public Map<String, Object> save(TransactionContext context, Map<String, Object> body) {
        return service.save(context, body);
    }
}
