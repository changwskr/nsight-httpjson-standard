package com.nh.nsight.marketing.demo.controller;

import com.nh.nsight.marketing.demo.service.UpdownloadRelayService;
import com.nh.nsight.marketing.demo.service.TransactionRelayService.RelayOptions;
import java.util.Map;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/updownload")
public class UpdownloadApiController {
    private final UpdownloadRelayService relayService;

    public UpdownloadApiController(UpdownloadRelayService relayService) {
        this.relayService = relayService;
    }

    @GetMapping("/base-url")
    public Map<String, String> baseUrl(
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return Map.of("baseUrl", relayService.resolveBaseUrl(options));
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "userId", required = false, defaultValue = "U123456") String userId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relayUpload(file, userId, description, options);
    }

    @GetMapping("/files")
    public String list(
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relayList(options);
    }

    @GetMapping("/files/{fileId}/download")
    public ResponseEntity<ByteArrayResource> download(
            @PathVariable("fileId") String fileId,
            @RequestParam(value = "deploymentMode", required = false) String deploymentMode,
            @RequestParam(value = "bootrunHost", required = false) String bootrunHost,
            @RequestParam(value = "tomcatGatewayUrl", required = false) String tomcatGatewayUrl) {
        RelayOptions options = new RelayOptions(deploymentMode, bootrunHost, tomcatGatewayUrl);
        return relayService.relayDownload(fileId, options);
    }
}
