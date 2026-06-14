package com.nh.nsight.marketing.common.web.controller;

import com.nh.nsight.marketing.common.message.StandardRequest;
import com.nh.nsight.marketing.common.message.StandardResponse;
import com.nh.nsight.marketing.common.web.processor.TCF;
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

    private final TCF tcf;

    public OnlineTransactionController(TCF tcf) {
        this.tcf = tcf;
    }

    @PostMapping(value = "/online", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Object>> process(
            @Valid @RequestBody StandardRequest<Map<String, Object>> request) {
        System.out.println("====================================================================[OnlineTransactionController.process] start");
        System.out.println("request: " + request);
        StandardResponse<Object> response = tcf.process(null, request);
        System.out.println("response: " + response);
        System.out.println("====================================================================[OnlineTransactionController.process] end");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{businessCode}/online", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StandardResponse<Object>> processWithBusinessCode(
            @PathVariable("businessCode") String businessCode,
            @Valid @RequestBody StandardRequest<Map<String, Object>> request) {
        System.out.println("====================================================================[OnlineTransactionController.processWithBusinessCode] start");
        System.out.println("businessCode: " + businessCode);
        System.out.println("request: " + request);
        StandardResponse<Object> response = tcf.process(businessCode, request);
        System.out.println("response: " + response);
        System.out.println("====================================================================[OnlineTransactionController.processWithBusinessCode] end");
        return ResponseEntity.ok(response);
    }
}
