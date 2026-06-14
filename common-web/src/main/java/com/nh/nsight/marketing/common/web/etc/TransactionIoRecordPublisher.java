package com.nh.nsight.marketing.common.web.etc;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.common.web.config.EtcRecordProperties;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TransactionIoRecordPublisher {
    private static final Logger log = LoggerFactory.getLogger(TransactionIoRecordPublisher.class);

    private final EtcRecordProperties properties;
    private final RestClient restClient;

    public TransactionIoRecordPublisher(EtcRecordProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void publish(TransactionContext context, StandardHeader responseHeader, Result result) {
        if (!properties.isRecordEnabled()) {
            return;
        }
        StandardHeader inputHeader = context.getRequestHeader() != null
                ? context.getRequestHeader()
                : context.getHeader().copy();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("inputHeader", inputHeader);
        payload.put("outputHeader", responseHeader);
        payload.put("result", result);
        try {
            restClient.post()
                    .uri(URI.create(properties.getRecordUrl()))
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.warn("Failed to publish transaction I/O record guid={} url={} message={}",
                    context.getGuid(), properties.getRecordUrl(), e.getMessage());
        }
    }
}
