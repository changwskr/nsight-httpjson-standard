package com.nh.nsight.marketing.updownload.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.dao.UpdownloadDao;
import com.nh.nsight.marketing.updownload.rule.UpdownloadRule;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpdownloadService {
    private final UpdownloadRule rule;
    private final UpdownloadDao dao;

    public UpdownloadService(UpdownloadRule rule, UpdownloadDao dao) {
        this.rule = rule;
        this.dao = dao;
    }

    public Map<String, Object> upload(TransactionContext context, Map<String, Object> body) {
        String fileName = stringValue(body.get("fileName"));
        String contentType = stringValue(body.get("contentType"));
        String contentBase64 = stringValue(body.get("contentBase64"));
        String description = stringValue(body.get("description"));
        byte[] content = decodeBase64(contentBase64);
        rule.validateUpload(context, body, content.length);
        Map<String, Object> saved = dao.insertUploadedFile(
                context,
                fileName,
                StringUtils.hasText(contentType) ? contentType : "application/octet-stream",
                content,
                description
        );
        return buildResult(context, "upload", saved);
    }

    public Map<String, Object> download(TransactionContext context, Map<String, Object> body) {
        rule.validateDownload(context, body);
        String fileId = stringValue(body.get("fileId"));
        Map<String, Object> fileMeta = dao.selectFileById(fileId);
        byte[] content = dao.readFileContent(fileMeta);
        Map<String, Object> result = buildResult(context, "download", fileMeta);
        result.put("contentBase64", Base64.getEncoder().encodeToString(content));
        return result;
    }

    public Map<String, Object> list(TransactionContext context, Map<String, Object> body) {
        rule.validateList(context);
        Map<String, Object> criteria = new HashMap<>(body == null ? Map.of() : body);
        rule.normalizeSearchCriteria(criteria);
        List<Map<String, Object>> files = dao.searchFiles(criteria);
        int totalCount = dao.countFiles(criteria);
        return buildListResult(context, criteria, files, totalCount);
    }

    public Map<String, Object> detail(TransactionContext context, Map<String, Object> body) {
        rule.validateDetail(context, body);
        String fileId = stringValue(body.get("fileId"));
        Map<String, Object> fileMeta = dao.selectFileById(fileId);
        return buildResult(context, "detail", fileMeta);
    }

    public Map<String, Object> update(TransactionContext context, Map<String, Object> body) {
        rule.validateUpdate(context, body);
        String fileId = stringValue(body.get("fileId"));
        String description = stringValue(body.get("description"));
        Map<String, Object> updated = dao.updateDescription(fileId, description);
        return buildResult(context, "update", updated);
    }

    public Map<String, Object> delete(TransactionContext context, Map<String, Object> body) {
        rule.validateDelete(context, body);
        String fileId = stringValue(body.get("fileId"));
        Map<String, Object> fileMeta = dao.selectFileById(fileId);
        dao.deleteFile(fileId);
        Map<String, Object> result = buildResult(context, "delete", fileMeta);
        result.put("deleted", true);
        return result;
    }

    public Map<String, Object> uploadMultipart(TransactionContext context, MultipartFile file, String description) {
        String fileName = file.getOriginalFilename();
        if (!StringUtils.hasText(fileName)) {
            fileName = "unknown.bin";
        }
        rule.validateUpload(context, Map.of("fileName", fileName), file.getSize());
        Map<String, Object> saved = dao.insertMultipartFile(context, file, description);
        return buildResult(context, "upload", saved);
    }

    public Map<String, Object> getFileMeta(String fileId) {
        rule.validateFileId(fileId);
        return dao.selectFileById(fileId);
    }

    public byte[] readFileBytes(String fileId) {
        Map<String, Object> fileMeta = getFileMeta(fileId);
        return dao.readFileContent(fileMeta);
    }

    public Map<String, Object> listFiles(Map<String, Object> criteria) {
        Map<String, Object> search = new HashMap<>(criteria == null ? Map.of() : criteria);
        rule.normalizeSearchCriteria(search);
        List<Map<String, Object>> files = dao.searchFiles(search);
        int totalCount = dao.countFiles(search);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("businessCode", search.get("businessCode"));
        body.put("pageNo", search.get("pageNo"));
        body.put("pageSize", search.get("pageSize"));
        body.put("totalCount", totalCount);
        body.put("files", files);
        return body;
    }

    public Map<String, Object> updateFileMeta(String fileId, String description) {
        rule.validateFileId(fileId);
        Map<String, Object> updated = dao.updateDescription(fileId, description);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("action", "update");
        body.put("file", updated);
        return body;
    }

    public Map<String, Object> deleteFileMeta(String fileId) {
        rule.validateFileId(fileId);
        Map<String, Object> fileMeta = dao.selectFileById(fileId);
        dao.deleteFile(fileId);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("action", "delete");
        body.put("deleted", true);
        body.put("file", fileMeta);
        return body;
    }

    private Map<String, Object> buildListResult(TransactionContext context, Map<String, Object> criteria,
                                                List<Map<String, Object>> files, int totalCount) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "UD");
        result.put("businessName", "Common UpDownload");
        result.put("businessGroup", "공통");
        result.put("description", "공통 파일 업로드/다운로드/관리");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("pageNo", criteria.get("pageNo"));
        result.put("pageSize", criteria.get("pageSize"));
        result.put("totalCount", totalCount);
        result.put("files", files);
        return result;
    }

    private Map<String, Object> buildResult(TransactionContext context, String action, Map<String, Object> fileMeta) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "UD");
        result.put("businessName", "Common UpDownload");
        result.put("businessGroup", "공통");
        result.put("description", "공통 파일 업로드/다운로드/관리");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("action", action);
        result.put("file", fileMeta);
        result.put("downloadUrl", "/ud/files/" + fileMeta.get("fileId") + "/download");
        return result;
    }

    private byte[] decodeBase64(String contentBase64) {
        if (!StringUtils.hasText(contentBase64)) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(contentBase64);
    }

    private String stringValue(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
