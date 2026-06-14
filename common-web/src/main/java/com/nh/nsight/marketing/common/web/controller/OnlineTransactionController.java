package com.nh.nsight.marketing.common.web.controller;

import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.web.processor.OnlineTransactionProcessor;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OnlineTransactionController {

    private final OnlineTransactionProcessor onlineTransactionProcessor;

    public OnlineTransactionController(OnlineTransactionProcessor onlineTransactionProcessor) {
        this.onlineTransactionProcessor = onlineTransactionProcessor;
    }

    @PostMapping(value = "/online", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Object>> process(
            @Valid @RequestBody StandardRequest<Map<String, Object>> request) {
        return ResponseEntity.ok(onlineTransactionProcessor.process(null, request));
    }

    @PostMapping(value = "/{businessCode}/online", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Object>> processWithBusinessCode(
            @PathVariable("businessCode") String businessCode,
            @Valid @RequestBody StandardRequest<Map<String, Object>> request) {
        return ResponseEntity.ok(onlineTransactionProcessor.process(businessCode, request));
    }
}
