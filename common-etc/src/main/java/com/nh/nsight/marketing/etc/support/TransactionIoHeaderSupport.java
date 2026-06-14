package com.nh.nsight.marketing.etc.support;

import com.nh.nsight.marketing.common.message.StandardHeader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class TransactionIoHeaderSupport {
    private static final List<String> HEADER_FIELDS = List.of(
            "systemId", "businessCode", "serviceId", "transactionCode", "processingType",
            "guid", "traceId", "channelId", "userId", "branchId", "centerId", "clientIp",
            "requestTime", "responseTime", "transactionIntime", "transactionOuttime",
            "systemDate", "bizDate", "apId"
    );

    private TransactionIoHeaderSupport() {
    }

    public static Map<String, Object> toMap(StandardHeader header) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (header == null) {
            return map;
        }
        map.put("systemId", header.getSystemId());
        map.put("businessCode", header.getBusinessCode());
        map.put("serviceId", header.getServiceId());
        map.put("transactionCode", header.getTransactionCode());
        map.put("processingType", header.getProcessingType());
        map.put("guid", header.getGuid());
        map.put("traceId", header.getTraceId());
        map.put("channelId", header.getChannelId());
        map.put("userId", header.getUserId());
        map.put("branchId", header.getBranchId());
        map.put("centerId", header.getCenterId());
        map.put("clientIp", header.getClientIp());
        map.put("requestTime", header.getRequestTime());
        map.put("responseTime", header.getResponseTime());
        map.put("transactionIntime", header.getTransactionIntime());
        map.put("transactionOuttime", header.getTransactionOuttime());
        map.put("systemDate", header.getSystemDate());
        map.put("bizDate", header.getBizDate());
        map.put("apId", header.getApId());
        return map;
    }

    public static Map<String, Object> extractInputHeader(Map<String, Object> row) {
        return extractPrefixedHeader(row, "in");
    }

    public static Map<String, Object> extractOutputHeader(Map<String, Object> row) {
        return extractPrefixedHeader(row, "out");
    }

    private static Map<String, Object> extractPrefixedHeader(Map<String, Object> row, String prefix) {
        Map<String, Object> header = new LinkedHashMap<>();
        for (String field : HEADER_FIELDS) {
            header.put(field, readValue(row, prefix + capitalize(field)));
        }
        return header;
    }

    public static void applyInputHeader(Map<String, Object> target, StandardHeader header) {
        applyPrefixedHeader(target, "in", header);
    }

    public static void applyOutputHeader(Map<String, Object> target, StandardHeader header) {
        applyPrefixedHeader(target, "out", header);
    }

    private static void applyPrefixedHeader(Map<String, Object> target, String prefix, StandardHeader header) {
        Map<String, Object> map = toMap(header);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            target.put(prefix + capitalize(entry.getKey()), entry.getValue());
        }
    }

    public static Object readValue(Map<String, Object> row, String fieldName) {
        if (row == null) {
            return null;
        }
        if (row.containsKey(fieldName)) {
            return row.get(fieldName);
        }
        String upper = fieldName.toUpperCase();
        if (row.containsKey(upper)) {
            return row.get(upper);
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(fieldName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static String capitalize(String field) {
        if (field == null || field.isEmpty()) {
            return field;
        }
        return Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }
}
