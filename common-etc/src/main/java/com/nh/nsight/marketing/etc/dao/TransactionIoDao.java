package com.nh.nsight.marketing.etc.dao;

import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.etc.mapper.TransactionIoMapper;
import com.nh.nsight.marketing.etc.support.TransactionIoHeaderSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class TransactionIoDao {
    private static final DateTimeFormatter RECORD_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final TransactionIoMapper mapper;

    public TransactionIoDao(TransactionIoMapper mapper) {
        this.mapper = mapper;
    }

    public Map<String, Object> insertLog(StandardHeader inputHeader, StandardHeader outputHeader, Result result) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("logId", UUID.randomUUID().toString().replace("-", ""));
        parameter.put("guid", outputHeader.getGuid());
        parameter.put("traceId", outputHeader.getTraceId());
        parameter.put("businessCode", outputHeader.getBusinessCode());
        parameter.put("serviceId", outputHeader.getServiceId());
        parameter.put("transactionCode", outputHeader.getTransactionCode());
        parameter.put("systemDate", outputHeader.getSystemDate());
        parameter.put("resultStatus", result.getStatus());
        parameter.put("resultCode", result.getResultCode());
        parameter.put("errorCode", result.getErrorCode());
        parameter.put("elapsedTimeMs", result.getElapsedTimeMs());
        parameter.put("recordTime", LocalDateTime.now().format(RECORD_TIME));
        TransactionIoHeaderSupport.applyInputHeader(parameter, inputHeader);
        TransactionIoHeaderSupport.applyOutputHeader(parameter, outputHeader);
        mapper.insertLog(parameter);
        return selectLogById(String.valueOf(parameter.get("logId")));
    }

    public Map<String, Object> selectLogById(String logId) {
        Map<String, Object> row = mapper.selectLogById(Map.of("logId", logId));
        if (row == null || row.isEmpty()) {
            throw new BusinessException("E-ET-IO-0002", "트랜잭션 입출력 정보를 찾을 수 없습니다: " + logId);
        }
        return row;
    }

    public Map<String, Object> selectLogByGuid(String guid) {
        Map<String, Object> row = mapper.selectLogByGuid(Map.of("guid", guid));
        if (row == null || row.isEmpty()) {
            throw new BusinessException("E-ET-IO-0002", "트랜잭션 입출력 정보를 찾을 수 없습니다: " + guid);
        }
        return row;
    }

    public List<Map<String, Object>> selectLogList(Map<String, Object> criteria) {
        Map<String, Object> parameter = buildListCriteria(criteria);
        return mapper.selectLogList(parameter);
    }

    public Map<String, Object> selectLogPage(Map<String, Object> criteria) {
        Map<String, Object> safeCriteria = criteria == null ? Map.of() : criteria;
        int page = parsePositiveInt(safeCriteria.get("page"), 1);
        int pageSize = parsePositiveInt(safeCriteria.get("pageSize"), 3);
        int offset = (page - 1) * pageSize;

        Map<String, Object> parameter = buildListCriteria(safeCriteria);
        long totalCount = mapper.countLogList(parameter);

        parameter.put("limit", pageSize);
        parameter.put("offset", offset);
        List<Map<String, Object>> rows = mapper.selectLogList(parameter);

        int totalPages = totalCount == 0 ? 0 : (int) ((totalCount + pageSize - 1) / pageSize);
        Map<String, Object> pageResult = new HashMap<>();
        pageResult.put("page", page);
        pageResult.put("pageSize", pageSize);
        pageResult.put("totalCount", totalCount);
        pageResult.put("totalPages", totalPages);
        pageResult.put("hasNext", page < totalPages);
        pageResult.put("rows", rows);
        return pageResult;
    }

    public int deleteAllLogs() {
        return mapper.deleteAllLogs();
    }

    private Map<String, Object> buildListCriteria(Map<String, Object> criteria) {
        Map<String, Object> parameter = new HashMap<>();
        copyIfPresent(criteria, parameter, "guid");
        copyIfPresent(criteria, parameter, "traceId");
        copyIfPresent(criteria, parameter, "businessCode");
        copyIfPresent(criteria, parameter, "serviceId");
        copyIfPresent(criteria, parameter, "systemDate");
        return parameter;
    }

    private int parsePositiveInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            int parsed = Integer.parseInt(String.valueOf(value));
            return parsed > 0 ? parsed : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void copyIfPresent(Map<String, Object> source, Map<String, Object> target, String key) {
        Object value = source.get(key);
        if (value != null && StringUtils.hasText(String.valueOf(value))) {
            target.put(key, String.valueOf(value));
        }
    }
}
