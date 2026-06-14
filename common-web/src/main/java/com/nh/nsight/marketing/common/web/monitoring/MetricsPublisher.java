package com.nh.nsight.marketing.common.web.monitoring;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MetricsPublisher {
    private static final Logger log = LoggerFactory.getLogger(MetricsPublisher.class);

    public void publish(TransactionContext context, Result result) {
        log.debug("METRIC serviceId={} status={} elapsedMs={}", context.getServiceId(), result.getStatus(), result.getElapsedTimeMs());
    }
}
