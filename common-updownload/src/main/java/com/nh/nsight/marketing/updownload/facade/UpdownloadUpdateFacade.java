package com.nh.nsight.marketing.updownload.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdownloadUpdateFacade {
    private final UpdownloadService service;

    public UpdownloadUpdateFacade(UpdownloadService service) {
        this.service = service;
    }

    @Transactional(timeout = 10)
    public Map<String, Object> update(TransactionContext context, Map<String, Object> body) {
        return service.update(context, body);
    }
}
