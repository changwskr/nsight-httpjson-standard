package com.nh.nsight.marketing.demo.model;

public record RelayResult(
        String businessCode,
        String targetUrl,
        int httpStatus,
        long elapsedMs,
        String responseBody
) {
}
