package com.nh.nsight.marketing.demo.service;

import com.nh.nsight.marketing.demo.config.DemoUiProperties;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import com.nh.nsight.marketing.demo.service.TransactionRelayService.RelayOptions;
import java.net.URI;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UpdownloadRelayService {
    private static final String BUSINESS_CODE = "UD";

    private final DemoUiProperties properties;
    private final BusinessModuleCatalog catalog;
    private final RestClient restClient;

    public UpdownloadRelayService(DemoUiProperties properties, BusinessModuleCatalog catalog) {
        this.properties = properties;
        this.catalog = catalog;
        this.restClient = RestClient.builder().build();
    }

    public String resolveBaseUrl(RelayOptions options) {
        BusinessModuleInfo module = catalog.findByCode(BUSINESS_CODE);
        if (resolveMode(options) == DemoUiProperties.DeploymentMode.tomcat) {
            return trimTrailingSlash(resolveTomcatGateway(options)) + module.contextPath();
        }
        return trimTrailingSlash(resolveBootrunHost(options)) + ":" + module.localPort();
    }

    public String relayUpload(MultipartFile file, String userId, String description, RelayOptions options) {
        String targetUrl = resolveBaseUrl(options) + "/ud/files/upload";
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());
        if (StringUtils.hasText(userId)) {
            body.add("userId", userId);
        }
        if (StringUtils.hasText(description)) {
            body.add("description", description);
        }
        try {
            return restClient.post()
                    .uri(URI.create(targetUrl))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return e.getResponseBodyAsString();
        } catch (Exception e) {
            return connectionErrorJson(targetUrl, e);
        }
    }

    public ResponseEntity<ByteArrayResource> relayDownload(String fileId, RelayOptions options) {
        String targetUrl = resolveBaseUrl(options) + "/ud/files/" + fileId + "/download";
        try {
            ResponseEntity<byte[]> response = restClient.get()
                    .uri(URI.create(targetUrl))
                    .retrieve()
                    .toEntity(byte[].class);
            byte[] content = response.getBody() == null ? new byte[0] : response.getBody();
            return ResponseEntity.status(response.getStatusCode())
                    .headers(headers -> headers.putAll(response.getHeaders()))
                    .body(new ByteArrayResource(content));
        } catch (RestClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ByteArrayResource(e.getResponseBodyAsByteArray()));
        } catch (Exception e) {
            byte[] body = connectionErrorJson(targetUrl, e).getBytes(java.nio.charset.StandardCharsets.UTF_8);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ByteArrayResource(body));
        }
    }

    public String relayList(RelayOptions options) {
        String targetUrl = resolveBaseUrl(options) + "/ud/files?businessCode=UD";
        try {
            return restClient.get()
                    .uri(URI.create(targetUrl))
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException e) {
            return e.getResponseBodyAsString();
        } catch (Exception e) {
            return connectionErrorJson(targetUrl, e);
        }
    }

    private String connectionErrorJson(String targetUrl, Exception e) {
        String message = e.getMessage() == null ? "대상 서비스에 연결할 수 없습니다." : e.getMessage();
        return """
                {
                  "header": { "businessCode": "UD" },
                  "body": {
                    "error": "%s",
                    "targetUrl": "%s",
                    "hint": "common-updownload(포트 8097)를 기동했는지 확인하세요. 예) gradle :common-updownload:bootRun",
                    "files": [],
                    "totalCount": 0
                  }
                }
                """.formatted(escapeJson(message), escapeJson(targetUrl));
    }

    private DemoUiProperties.DeploymentMode resolveMode(RelayOptions options) {
        if (options != null && StringUtils.hasText(options.deploymentMode())) {
            return DemoUiProperties.DeploymentMode.valueOf(options.deploymentMode());
        }
        return properties.getDeploymentMode();
    }

    private String resolveTomcatGateway(RelayOptions options) {
        if (options != null && StringUtils.hasText(options.tomcatGatewayUrl())) {
            return options.tomcatGatewayUrl();
        }
        return properties.getTomcatGatewayUrl();
    }

    private String resolveBootrunHost(RelayOptions options) {
        if (options != null && StringUtils.hasText(options.bootrunHost())) {
            return options.bootrunHost();
        }
        return properties.getBootrunHost();
    }

    private String trimTrailingSlash(String value) {
        if (!StringUtils.hasText(value)) {
            return "http://localhost";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
