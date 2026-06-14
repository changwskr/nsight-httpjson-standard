package com.nh.nsight.marketing.demo.service;

import com.nh.nsight.marketing.demo.config.DemoUiProperties;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import com.nh.nsight.marketing.demo.model.RelayResult;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class TransactionRelayService {
    private final DemoUiProperties properties;
    private final BusinessModuleCatalog catalog;
    private final RestClient restClient;

    public TransactionRelayService(DemoUiProperties properties, BusinessModuleCatalog catalog) {
        this.properties = properties;
        this.catalog = catalog;
        this.restClient = RestClient.builder()
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String resolveTargetUrl(String businessCode, RelayOptions options) {
        BusinessModuleInfo module = catalog.findByCode(businessCode);
        String baseUrl = resolveBaseUrl(module, options);
        return baseUrl + module.contextPath() + "/online";
    }

    private String resolveBaseUrl(BusinessModuleInfo module, RelayOptions options) {
        if (resolveMode(options) == DemoUiProperties.DeploymentMode.tomcat) {
            return trimTrailingSlash(resolveTomcatGateway(options));
        }
        return trimTrailingSlash(resolveBootrunHost(options)) + ":" + module.localPort();
    }

    public RelayResult relay(String businessCode, String requestBody, RelayOptions options) {
        String targetUrl = resolveTargetUrl(businessCode, options);
        long started = System.currentTimeMillis();
        try {
            String responseBody = restClient.post()
                    .uri(URI.create(targetUrl))
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);
            return new RelayResult(
                    businessCode.toUpperCase(),
                    targetUrl,
                    200,
                    System.currentTimeMillis() - started,
                    responseBody == null ? "" : responseBody
            );
        } catch (RestClientResponseException e) {
            return new RelayResult(
                    businessCode.toUpperCase(),
                    targetUrl,
                    e.getStatusCode().value(),
                    System.currentTimeMillis() - started,
                    e.getResponseBodyAsString()
            );
        } catch (Exception e) {
            return new RelayResult(
                    businessCode.toUpperCase(),
                    targetUrl,
                    502,
                    System.currentTimeMillis() - started,
                    "{\"error\":\"" + escapeJson(e.getMessage()) + "\"}"
            );
        }
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
        if (value == null || value.isBlank()) {
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

    public record RelayOptions(String deploymentMode, String bootrunHost, String tomcatGatewayUrl) {
    }
}
