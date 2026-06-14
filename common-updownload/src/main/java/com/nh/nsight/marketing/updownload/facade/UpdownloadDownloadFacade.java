package com.nh.nsight.marketing.updownload.facade;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdownloadDownloadFacade {
    private final UpdownloadService service;

    public UpdownloadDownloadFacade(UpdownloadService service) {
        this.service = service;
    }

    @Transactional(readOnly = true, timeout = 10)
    public Map<String, Object> download(TransactionContext context, Map<String, Object> body) {
        return service.download(context, body);
    }
}
