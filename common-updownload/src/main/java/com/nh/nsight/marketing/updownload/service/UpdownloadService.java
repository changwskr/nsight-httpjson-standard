package com.nh.nsight.marketing.updownload.service;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.updownload.dao.UpdownloadDao;
import com.nh.nsight.marketing.updownload.rule.UpdownloadRule;
import java.util.Base64;
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
        List<Map<String, Object>> files = dao.selectFileList(context.getBusinessCode());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "UD");
        result.put("businessName", "Common UpDownload");
        result.put("businessGroup", "공통");
        result.put("description", "공통 파일 업로드/다운로드");
        result.put("serviceId", context.getServiceId());
        result.put("transactionCode", context.getTransactionCode());
        result.put("totalCount", files.size());
        result.put("files", files);
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

    public List<Map<String, Object>> listFiles(String businessCode) {
        return dao.selectFileList(businessCode);
    }

    private Map<String, Object> buildResult(TransactionContext context, String action, Map<String, Object> fileMeta) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("businessCode", "UD");
        result.put("businessName", "Common UpDownload");
        result.put("businessGroup", "공통");
        result.put("description", "공통 파일 업로드/다운로드");
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
