package com.nh.nsight.marketing.demo.service;

import com.nh.nsight.marketing.demo.config.DemoUiProperties;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import com.nh.nsight.marketing.demo.service.TransactionRelayService.RelayOptions;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class EtcRelayService {
    private static final String BUSINESS_CODE = "ET";

    private final DemoUiProperties properties;
    private final BusinessModuleCatalog catalog;
    private final RestClient restClient;

    public EtcRelayService(DemoUiProperties properties, BusinessModuleCatalog catalog) {
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

    public String relayDeleteAllLogs(RelayOptions options) {
        String targetUrl = resolveBaseUrl(options) + "/et/transaction-io/logs/delete";
        try {
            return restClient.post()
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
                  "body": {
                    "error": "%s",
                    "targetUrl": "%s",
                    "hint": "common-etc(포트 8098)를 기동했는지 확인하세요. 예) gradle :common-etc:bootRun"
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
