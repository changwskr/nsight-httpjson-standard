package com.nh.nsight.marketing.demo.controller;

import com.nh.nsight.marketing.demo.config.DemoUiProperties;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import com.nh.nsight.marketing.demo.model.BusinessModuleTransactions;
import com.nh.nsight.marketing.demo.model.BusinessTransactionInfo;
import com.nh.nsight.marketing.demo.model.RelayResult;
import com.nh.nsight.marketing.demo.service.BusinessModuleCatalog;
import com.nh.nsight.marketing.demo.service.BusinessTransactionCatalog;
import com.nh.nsight.marketing.demo.service.TransactionRelayService;
import com.nh.nsight.marketing.demo.service.TransactionRelayService.RelayOptions;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoApiController {
    private final BusinessModuleCatalog catalog;
    private final BusinessTransactionCatalog transactionCatalog;
    private final TransactionRelayService relayService;
    private final DemoUiProperties properties;

    public DemoApiController(BusinessModuleCatalog catalog, BusinessTransactionCatalog transactionCatalog,
                             TransactionRelayService relayService, DemoUiProperties properties) {
        this.catalog = catalog;
        this.transactionCatalog = transactionCatalog;
        this.relayService = relayService;
        this.properties = properties;
    }

    @GetMapping("/business-modules")
    public List<BusinessModuleInfo> businessModules() {
        return catalog.findAll();
    }

    @GetMapping("/business-modules/{code}")
    public BusinessModuleInfo businessModule(@PathVariable("code") String code) {
        return catalog.findByCode(code);
    }

    @GetMapping("/multi/business-modules")
    public List<BusinessModuleTransactions> multiBusinessModules() {
        return transactionCatalog.findAll();
    }

    @GetMapping("/multi/business-modules/{code}")
    public BusinessModuleTransactions multiBusinessModule(@PathVariable("code") String code) {
        return transactionCatalog.findByCode(code);
    }

    @GetMapping("/multi/business-modules/{code}/transactions/{transactionId}")
    public BusinessTransactionInfo multiTransaction(
            @PathVariable("code") String code,
            @PathVariable("transactionId") String transactionId) {
        return transactionCatalog.findTransaction(code, transactionId);
    }

    @GetMapping("/multi/business-modules/{code}/target-url")
    public Map<String, String> multiTargetUrl(
            @PathVariable("code") String code,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return Map.of("targetUrl", relayService.resolveTargetUrl(code, options));
    }

    @PostMapping("/multi/relay/{code}/online")
    public RelayResult multiRelay(
            @PathVariable("code") String code,
            @RequestBody String requestBody,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relay(code, requestBody, options);
    }

    @GetMapping("/config")
    public Map<String, Object> config() {
        Map<String, Object> config = new LinkedHashMap<>();
        config.put("deploymentMode", properties.getDeploymentMode().name());
        config.put("tomcatGatewayUrl", properties.getTomcatGatewayUrl());
        config.put("bootrunHost", properties.getBootrunHost());
        return config;
    }

    @GetMapping("/business-modules/{code}/target-url")
    public Map<String, String> targetUrl(
            @PathVariable("code") String code,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return Map.of("targetUrl", relayService.resolveTargetUrl(code, options));
    }

    @PostMapping("/relay/{code}/online")
    public RelayResult relay(
            @PathVariable("code") String code,
            @RequestBody String requestBody,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relay(code, requestBody, options);
    }
}
