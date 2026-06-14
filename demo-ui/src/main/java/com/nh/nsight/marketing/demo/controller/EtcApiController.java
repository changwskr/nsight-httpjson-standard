package com.nh.nsight.marketing.demo.controller;

import com.nh.nsight.marketing.demo.service.EtcRelayService;
import com.nh.nsight.marketing.demo.service.TransactionRelayService.RelayOptions;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/etc")
public class EtcApiController {
    private final EtcRelayService relayService;

    public EtcApiController(EtcRelayService relayService) {
        this.relayService = relayService;
    }

    @DeleteMapping("/transaction-logs")
    public String deleteAllLogs(
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relayDeleteAllLogs(options);
    }

    @PostMapping("/transaction-logs/delete")
    public String deleteAllLogsPost(
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        return deleteAllLogs(deploymentMode, bootrunHost, tomcatGatewayUrl);
    }
}
