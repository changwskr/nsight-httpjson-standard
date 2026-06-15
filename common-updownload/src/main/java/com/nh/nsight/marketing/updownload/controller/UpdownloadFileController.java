package com.nh.nsight.marketing.updownload.controller;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ud/files")
public class UpdownloadFileController {
    private final UpdownloadService service;

    public UpdownloadFileController(UpdownloadService service) {
        this.service = service;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false, defaultValue = "U123456") String userId,
            @RequestParam(value = "description", required = false) String description) {
        TransactionContext context = buildContext(userId, "UD.File.upload", "UD-UPL-0001", "CREATE");
        Map<String, Object> result = service.uploadMultipart(context, file, description);
        return Map.of("header", context.getHeader(), "body", result);
    }

    @GetMapping("/{fileId}/download")
    public ResponseEntity<ByteArrayResource> download(@PathVariable("fileId") String fileId) {
        Map<String, Object> fileMeta = service.getFileMeta(fileId);
        byte[] content = service.readFileBytes(fileId);
        String originalName = String.valueOf(fileMeta.get("originalName"));
        String contentType = String.valueOf(fileMeta.getOrDefault("contentType", MediaType.APPLICATION_OCTET_STREAM_VALUE));
        String encodedName = URLEncoder.encode(originalName, StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedName)
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(content.length)
                .body(new ByteArrayResource(content));
    }

    @GetMapping("/{fileId}")
    public Map<String, Object> detail(@PathVariable("fileId") String fileId) {
        TransactionContext context = buildContext("U123456", "UD.File.detail", "UD-DTL-0001", "INQUIRY");
        Map<String, Object> result = service.detail(context, Map.of("fileId", fileId));
        return Map.of("header", context.getHeader(), "body", result);
    }

    @PutMapping("/{fileId}")
    public Map<String, Object> update(
            @PathVariable("fileId") String fileId,
            @RequestBody(required = false) Map<String, Object> body) {
        TransactionContext context = buildContext("U123456", "UD.File.update", "UD-UPD-0001", "UPDATE");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fileId", fileId);
        if (body != null) {
            requestBody.putAll(body);
        }
        Map<String, Object> result = service.update(context, requestBody);
        return Map.of("header", context.getHeader(), "body", result);
    }

    @DeleteMapping("/{fileId}")
    public Map<String, Object> delete(@PathVariable("fileId") String fileId) {
        TransactionContext context = buildContext("U123456", "UD.File.delete", "UD-DEL-0001", "DELETE");
        Map<String, Object> result = service.delete(context, Map.of("fileId", fileId));
        return Map.of("header", context.getHeader(), "body", result);
    }

    @GetMapping
    public Map<String, Object> list(
            @RequestParam(value = "businessCode", defaultValue = "UD") String businessCode,
            @RequestParam(value = "originalName", required = false) String originalName,
            @RequestParam(value = "uploadUser", required = false) String uploadUser,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        Map<String, Object> criteria = new LinkedHashMap<>();
        criteria.put("businessCode", businessCode);
        criteria.put("originalName", originalName);
        criteria.put("uploadUser", uploadUser);
        criteria.put("fromDate", fromDate);
        criteria.put("toDate", toDate);
        criteria.put("pageNo", pageNo);
        criteria.put("pageSize", pageSize);
        Map<String, Object> body = service.listFiles(criteria);
        return Map.of("header", buildHeader("UD.File.list"), "body", body);
    }

    private TransactionContext buildContext(String userId, String serviceId, String transactionCode, String processingType) {
        StandardHeader header = new StandardHeader();
        header.setSystemId("NSIGHT-MP");
        header.setBusinessCode("UD");
        header.setServiceId(serviceId);
        header.setTransactionCode(transactionCode);
        header.setProcessingType(processingType);
        header.setUserId(userId);
        header.setChannelId("WEBTOP");
        return new TransactionContext(header, Instant.now());
    }

    private StandardHeader buildHeader(String serviceId) {
        StandardHeader header = new StandardHeader();
        header.setSystemId("NSIGHT-MP");
        header.setBusinessCode("UD");
        header.setServiceId(serviceId);
        header.setTransactionCode("UD-LST-0001");
        header.setProcessingType("INQUIRY");
        return header;
    }
}
