package com.nh.nsight.marketing.demo.model;

import java.util.Map;

public record BusinessTransactionInfo(
        String id,
        String label,
        String serviceId,
        String transactionCode,
        String processingType,
        Map<String, Object> sampleRequest
) {
}
