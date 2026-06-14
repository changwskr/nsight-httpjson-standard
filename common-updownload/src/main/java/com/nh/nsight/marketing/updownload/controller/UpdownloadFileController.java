package com.nh.nsight.marketing.updownload.controller;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.message.StandardHeader;
import com.nh.nsight.marketing.updownload.service.UpdownloadService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        TransactionContext context = buildContext(userId);
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

    @GetMapping
    public Map<String, Object> list(@RequestParam(value = "businessCode", defaultValue = "UD") String businessCode) {
        List<Map<String, Object>> files = service.listFiles(businessCode);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("businessCode", businessCode);
        body.put("totalCount", files.size());
        body.put("files", files);
        return Map.of("header", buildHeader("UD.File.list"), "body", body);
    }

    private TransactionContext buildContext(String userId) {
        StandardHeader header = new StandardHeader();
        header.setSystemId("NSIGHT-MP");
        header.setBusinessCode("UD");
        header.setServiceId("UD.File.upload");
        header.setTransactionCode("UD-UPL-0001");
        header.setProcessingType("CREATE");
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
