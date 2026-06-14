package com.nh.nsight.marketing.etc.controller;

import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.etc.service.TransactionIoService;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/et/transaction-io")
public class TransactionIoRecordController {
    private final TransactionIoService service;

    public TransactionIoRecordController(TransactionIoService service) {
        this.service = service;
    }

    @PostMapping("/record")
    public Map<String, Object> record(@RequestBody TransactionIoRecordRequest request) {
        Map<String, Object> saved = service.record(
                request.inputHeader(),
                request.outputHeader(),
                toResult(request.result())
        );
        return Map.of("body", saved);
    }

    @DeleteMapping("/logs")
    public Map<String, Object> deleteAll() {
        return Map.of("body", service.deleteAll());
    }

    @PostMapping("/logs/delete")
    public Map<String, Object> deleteAllByPost() {
        return deleteAll();
    }

    private Result toResult(ResultPayload payload) {
        if (payload == null) {
            return Result.success(0L);
        }
        Result result = new Result();
        result.setStatus(payload.status());
        result.setResultCode(payload.resultCode());
        result.setMessageCode(payload.messageCode());
        result.setMessage(payload.message());
        result.setErrorCode(payload.errorCode());
        result.setErrorMessage(payload.errorMessage());
        result.setElapsedTimeMs(payload.elapsedTimeMs() == null ? 0L : payload.elapsedTimeMs());
        return result;
    }

    public record TransactionIoRecordRequest(
            StandardHeader inputHeader,
            StandardHeader outputHeader,
            ResultPayload result
    ) {
    }

    public record ResultPayload(
            String status,
            String resultCode,
            String messageCode,
            String message,
            String errorCode,
            String errorMessage,
            Long elapsedTimeMs
    ) {
    }
}
