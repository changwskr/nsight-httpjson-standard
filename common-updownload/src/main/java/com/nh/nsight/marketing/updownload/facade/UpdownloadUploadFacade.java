package com.nh.nsight.marketing.updownload.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdownloadUploadFacade {
    private final UpdownloadService service;

    public UpdownloadUploadFacade(UpdownloadService service) {
        this.service = service;
    }

    @Transactional(timeout = 10)
    public Map<String, Object> upload(TransactionContext context, Map<String, Object> body) {
        return service.upload(context, body);
    }
}
