package com.nh.nsight.marketing.updownload.rule;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.updownload.config.UpdownloadProperties;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UpdownloadRule {
    private final UpdownloadProperties properties;

    public UpdownloadRule(UpdownloadProperties properties) {
        this.properties = properties;
    }

    public void validateBusinessCode(TransactionContext context) {
        if (!"UD".equalsIgnoreCase(context.getBusinessCode())) {
            throw new BusinessException("E-UD-BIZ-0001", "업무코드가 UD가 아닙니다.");
        }
    }

    public void validateUpload(TransactionContext context, Map<String, Object> body, long fileSize) {
        validateBusinessCode(context);
        if (fileSize <= 0) {
            throw new BusinessException("E-UD-UPL-0001", "업로드 파일이 비어 있습니다.");
        }
        if (fileSize > properties.getMaxFileSizeBytes()) {
            throw new BusinessException("E-UD-UPL-0002", "허용된 최대 파일 크기를 초과했습니다.");
        }
        String fileName = stringValue(body.get("fileName"));
        if (!StringUtils.hasText(fileName)) {
            throw new BusinessException("E-UD-UPL-0003", "fileName은 필수입니다.");
        }
    }

    public void validateDownload(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
        if (!StringUtils.hasText(stringValue(body.get("fileId")))) {
            throw new BusinessException("E-UD-DWN-0001", "fileId는 필수입니다.");
        }
    }

    public void validateList(TransactionContext context) {
        validateBusinessCode(context);
    }

    public void validateDetail(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
        validateFileIdInBody(body);
    }

    public void validateUpdate(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
        validateFileIdInBody(body);
    }

    public void validateDelete(TransactionContext context, Map<String, Object> body) {
        validateBusinessCode(context);
        validateFileIdInBody(body);
    }

    public void validateFileId(String fileId) {
        if (!StringUtils.hasText(fileId)) {
            throw new BusinessException("E-UD-DWN-0001", "fileId는 필수입니다.");
        }
    }

    public void normalizeSearchCriteria(Map<String, Object> body) {
        if (!StringUtils.hasText(stringValue(body.get("businessCode")))) {
            body.put("businessCode", "UD");
        }
        int pageNo = Math.max(1, toInt(body.get("pageNo"), 1));
        int pageSize = Math.min(100, Math.max(1, toInt(body.get("pageSize"), 20)));
        body.put("pageNo", pageNo);
        body.put("pageSize", pageSize);
        body.put("offset", (pageNo - 1) * pageSize);
    }

    private void validateFileIdInBody(Map<String, Object> body) {
        if (!StringUtils.hasText(stringValue(body.get("fileId")))) {
            throw new BusinessException("E-UD-DWN-0001", "fileId는 필수입니다.");
        }
    }

    private int toInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(String.valueOf(value));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
