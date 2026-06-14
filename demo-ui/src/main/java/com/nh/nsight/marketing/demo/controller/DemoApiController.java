package com.nh.nsight.marketing.demo.controller;

import com.nh.nsight.marketing.demo.config.DemoUiProperties;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import com.nh.nsight.marketing.demo.model.RelayResult;
import com.nh.nsight.marketing.demo.service.BusinessModuleCatalog;
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
    private final TransactionRelayService relayService;
    private final DemoUiProperties properties;

    public DemoApiController(BusinessModuleCatalog catalog, TransactionRelayService relayService,
                             DemoUiProperties properties) {
        this.catalog = catalog;
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
