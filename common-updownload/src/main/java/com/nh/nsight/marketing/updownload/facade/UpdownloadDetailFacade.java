package com.nh.nsight.marketing.updownload.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdownloadDetailFacade {
    private final UpdownloadService service;

    public UpdownloadDetailFacade(UpdownloadService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 5)
    public Map<String, Object> detail(TransactionContext context, Map<String, Object> body) {
        return service.detail(context, body);
    }
}
