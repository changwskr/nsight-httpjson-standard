package com.nh.nsight.marketing.etc.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.Result;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.etc.dao.TransactionIoDao;
import com.nh.nsight.marketing.etc.rule.TransactionIoRule;
import com.nh.nsight.marketing.etc.support.TransactionIoHeaderSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TransactionIoService {
    private final TransactionIoRule rule;
    private final TransactionIoDao dao;

    public TransactionIoService(TransactionIoRule rule, TransactionIoDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> record(StandardHeader inputHeader, StandardHeader outputHeader, Result result) {
        Map<String, Object> saved = dao.insertLog(inputHeader, outputHeader, result);
        return toDetailResult(null, saved);
    }

    public Map<String, Object> list(TransactionContext context, Map<String, Object> body) {
        rule.validateList(context, body);
        Map<String, Object> safeBody = body == null ? Map.of() : body;
        Map<String, Object> pageResult = dao.selectLogPage(safeBody);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rows = (List<Map<String, Object>>) pageResult.get("rows");

        Map<String, Object> result = baseResult(context, "list");
        result.put("page", pageResult.get("page"));
        result.put("pageSize", pageResult.get("pageSize"));
        result.put("totalCount", pageResult.get("totalCount"));
        result.put("totalPages", pageResult.get("totalPages"));
        result.put("hasNext", pageResult.get("hasNext"));
        result.put("transactions", rows.stream().map(this::toSummary).collect(Collectors.toList()));
        return result;
    }

    public Map<String, Object> detail(TransactionContext context, Map<String, Object> body) {
        rule.validateDetail(context, body);
        Map<String, Object> row;
        if (StringUtils.hasText(stringValue(body.get("logId")))) {
            row = dao.selectLogById(stringValue(body.get("logId")));
        } else {
            row = dao.selectLogByGuid(stringValue(body.get("guid")));
        }
        return toDetailResult(context, row);
    }

    @Transactional
    public Map<String, Object> deleteAll() {
        return deleteAll(null, Map.of());
    }

    public Map<String, Object> deleteAll(TransactionContext context, Map<String, Object> body) {
        if (context != null) {
            rule.validateList(context, body == null ? Map.of() : body);
        }
        int deletedCount = dao.deleteAllLogs();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "ET");
        result.put("action", "deleteAll");
        result.put("deletedCount", deletedCount);
        result.put("message", "TX_IO_LOG 데이터가 삭제되었습니다.");
        if (context != null) {
            result.put("serviceId", context.getServiceId());
            result.put("transactionCode", context.getTransactionCode());
        }
        return result;
    }

    private Map<String, Object> toSummary(Map<String, Object> row) {
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("logId", TransactionIoHeaderSupport.readValue(row, "logId"));
        summary.put("guid", TransactionIoHeaderSupport.readValue(row, "guid"));
        summary.put("traceId", TransactionIoHeaderSupport.readValue(row, "traceId"));
        summary.put("businessCode", TransactionIoHeaderSupport.readValue(row, "businessCode"));
        summary.put("serviceId", TransactionIoHeaderSupport.readValue(row, "serviceId"));
        summary.put("transactionCode", TransactionIoHeaderSupport.readValue(row, "transactionCode"));
        summary.put("systemDate", TransactionIoHeaderSupport.readValue(row, "systemDate"));
        summary.put("resultStatus", TransactionIoHeaderSupport.readValue(row, "resultStatus"));
        summary.put("resultCode", TransactionIoHeaderSupport.readValue(row, "resultCode"));
        summary.put("errorCode", TransactionIoHeaderSupport.readValue(row, "errorCode"));
        summary.put("elapsedTimeMs", TransactionIoHeaderSupport.readValue(row, "elapsedTimeMs"));
        summary.put("recordTime", TransactionIoHeaderSupport.readValue(row, "recordTime"));
        summary.put("inputHeader", TransactionIoHeaderSupport.extractInputHeader(row));
        summary.put("outputHeader", TransactionIoHeaderSupport.extractOutputHeader(row));
        return summary;
    }

    private Map<String, Object> toDetailResult(TransactionContext context, Map<String, Object> row) {
        Map<String, Object> result = context == null ? new LinkedHashMap<>() : baseResult(context, "detail");
        result.put("logId", TransactionIoHeaderSupport.readValue(row, "logId"));
        result.put("guid", TransactionIoHeaderSupport.readValue(row, "guid"));
        result.put("traceId", TransactionIoHeaderSupport.readValue(row, "traceId"));
        result.put("businessCode", TransactionIoHeaderSupport.readValue(row, "businessCode"));
        result.put("serviceId", TransactionIoHeaderSupport.readValue(row, "serviceId"));
        result.put("transactionCode", TransactionIoHeaderSupport.readValue(row, "transactionCode"));
        result.put("systemDate", TransactionIoHeaderSupport.readValue(row, "systemDate"));
        result.put("resultStatus", TransactionIoHeaderSupport.readValue(row, "resultStatus"));
        result.put("resultCode", TransactionIoHeaderSupport.readValue(row, "resultCode"));
        result.put("errorCode", TransactionIoHeaderSupport.readValue(row, "errorCode"));
        result.put("elapsedTimeMs", TransactionIoHeaderSupport.readValue(row, "elapsedTimeMs"));
        result.put("recordTime", TransactionIoHeaderSupport.readValue(row, "recordTime"));
        result.put("inputHeader", TransactionIoHeaderSupport.extractInputHeader(row));
        result.put("outputHeader", TransactionIoHeaderSupport.extractOutputHeader(row));
        return result;
    }

    private Map<String, Object> baseResult(TransactionContext context, String action) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "ET");
        result.put("businessName", "Common ETC");
        result.put("businessGroup", "공통");
        result.put("description", "트랜잭션 입출력(StandardHeader) 조회");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("action", action);
        return result;
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
