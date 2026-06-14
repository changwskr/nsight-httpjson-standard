package com.nh.nsight.marketing.updownload.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class UpdownloadRowNormalizer {
    private static final List<String> FILE_FIELDS = List.of(
            "fileId",
            "originalName",
            "storedPath",
            "contentType",
            "fileSize",
            "uploadUser",
            "uploadTime",
            "businessCode",
            "description"
    );

    private UpdownloadRowNormalizer() {
    }

    static Map<String, Object> normalizeFileRow(Map<String, Object> row) {
        Map<String, Object> normalized = new LinkedHashMap<>();
        for (String field : FILE_FIELDS) {
            normalized.put(field, readValue(row, field));
        }
        return normalized;
    }

    static List<Map<String, Object>> normalizeFileRows(List<Map<String, Object>> rows) {
        return rows.stream().map(UpdownloadRowNormalizer::normalizeFileRow).toList();
    }

    static String readString(Map<String, Object> row, String field) {
        Object value = readValue(row, field);
        return value == null ? null : String.valueOf(value);
    }

    private static Object readValue(Map<String, Object> row, String field) {
        if (row == null || row.isEmpty()) {
            return null;
        }
        if (row.containsKey(field)) {
            return row.get(field);
        }
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(field)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
