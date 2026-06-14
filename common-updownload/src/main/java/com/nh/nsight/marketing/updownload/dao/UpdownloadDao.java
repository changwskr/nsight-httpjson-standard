package com.nh.nsight.marketing.updownload.dao;

import com.nh.nsight.marketing.common.context.TransactionContext;
import com.nh.nsight.marketing.common.exception.BusinessException;
import com.nh.nsight.marketing.updownload.config.UpdownloadProperties;
import com.nh.nsight.marketing.updownload.mapper.UpdownloadMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class UpdownloadDao {
    private static final DateTimeFormatter FILE_TIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final UpdownloadMapper mapper;
    private final UpdownloadProperties properties;

    public UpdownloadDao(UpdownloadMapper mapper, UpdownloadProperties properties) {
        this.mapper = mapper;
        this.properties = properties;
    }

    public Map<String, Object> insertUploadedFile(TransactionContext context, String originalName,
                                                  String contentType, byte[] content, String description) {
        String fileId = UUID.randomUUID().toString().replace("-", "");
        Path storageRoot = resolveStorageRoot();
        Path storedPath = storageRoot.resolve(fileId + "_" + sanitizeFileName(originalName));
        try {
            Files.createDirectories(storageRoot);
            Files.write(storedPath, content);
        } catch (IOException e) {
            throw new BusinessException("E-UD-UPL-0009", "파일 저장에 실패했습니다.");
        }

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("fileId", fileId);
        parameter.put("originalName", originalName);
        parameter.put("storedPath", storedPath.toString());
        parameter.put("contentType", contentType);
        parameter.put("fileSize", content.length);
        parameter.put("uploadUser", context.getHeader().getUserId());
        parameter.put("uploadTime", LocalDateTime.now().format(FILE_TIME));
        parameter.put("businessCode", context.getBusinessCode());
        parameter.put("description", description);
        mapper.insertFile(parameter);
        return selectFileById(fileId);
    }

    public Map<String, Object> insertMultipartFile(TransactionContext context, MultipartFile file, String description) {
        try {
            return insertUploadedFile(
                    context,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    description
            );
        } catch (IOException e) {
            throw new BusinessException("E-UD-UPL-0009", "파일 저장에 실패했습니다.");
        }
    }

    public Map<String, Object> selectFileById(String fileId) {
        Map<String, Object> parameter = Map.of("fileId", fileId);
        Map<String, Object> file = mapper.selectFileById(parameter);
        if (file == null || file.isEmpty()) {
            throw new BusinessException("E-UD-DWN-0002", "파일을 찾을 수 없습니다: " + fileId);
        }
        return UpdownloadRowNormalizer.normalizeFileRow(file);
    }

    public byte[] readFileContent(Map<String, Object> fileMeta) {
        String storedPath = UpdownloadRowNormalizer.readString(fileMeta, "storedPath");
        Path path = Paths.get(storedPath);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new BusinessException("E-UD-DWN-0003", "파일 읽기에 실패했습니다.");
        }
    }

    public List<Map<String, Object>> selectFileList(String businessCode) {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("businessCode", businessCode);
        return UpdownloadRowNormalizer.normalizeFileRows(mapper.selectFileList(parameter));
    }

    public Path resolveStoredPath(Map<String, Object> fileMeta) {
        return Paths.get(UpdownloadRowNormalizer.readString(fileMeta, "storedPath"));
    }

    private Path resolveStorageRoot() {
        return Paths.get(properties.getStoragePath()).toAbsolutePath().normalize();
    }

    private String sanitizeFileName(String originalName) {
        if (!StringUtils.hasText(originalName)) {
            return "unknown.bin";
        }
        return originalName.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
