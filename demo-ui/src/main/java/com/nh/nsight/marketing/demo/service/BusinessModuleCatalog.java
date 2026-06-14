package com.nh.nsight.marketing.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nh.nsight.marketing.demo.model.BusinessModuleInfo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class BusinessModuleCatalog {
    private static final List<ModuleDefinition> DEFINITIONS = List.of(
            new ModuleDefinition("CC", "Common", "공통", 8080),
            new ModuleDefinition("IC", "Integration Customer", "고객", 8081),
            new ModuleDefinition("PC", "Private Customer", "고객", 8082),
            new ModuleDefinition("BC", "Business Customer", "고객", 8083),
            new ModuleDefinition("MS", "Mini Single View", "고객", 8084),
            new ModuleDefinition("SV", "Single View", "마케팅", 8085),
            new ModuleDefinition("PD", "Product", "마케팅", 8086),
            new ModuleDefinition("CM", "Campaign", "마케팅", 8087),
            new ModuleDefinition("EB", "EBM", "마케팅", 8088),
            new ModuleDefinition("EP", "Event Processing", "실시간", 8089),
            new ModuleDefinition("BP", "Behavior Processing", "실시간", 8090),
            new ModuleDefinition("BD", "Behavior Data", "데이터", 8091),
            new ModuleDefinition("SS", "Sales Support", "지원", 8092),
            new ModuleDefinition("CS", "Common Service", "지원", 8093),
            new ModuleDefinition("CT", "Contents", "지원", 8094),
            new ModuleDefinition("MG", "Message", "지원", 8095),
            new ModuleDefinition("OM", "Operation Management", "운영", 8096),
            new ModuleDefinition("UD", "Common UpDownload", "공통", 8097),
            new ModuleDefinition("ET", "Common ETC", "공통", 8098)
    );

    private final List<BusinessModuleInfo> modules;

    public BusinessModuleCatalog(ObjectMapper objectMapper) {
        this.modules = loadModules(objectMapper);
    }

    public List<BusinessModuleInfo> findAll() {
        return modules;
    }

    public BusinessModuleInfo findByCode(String code) {
        return modules.stream()
                .filter(module -> module.code().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 업무코드입니다: " + code));
    }

    private List<BusinessModuleInfo> loadModules(ObjectMapper objectMapper) {
        List<BusinessModuleInfo> loaded = new ArrayList<>();
        for (ModuleDefinition definition : DEFINITIONS) {
            String code = definition.code();
            String fileName = "sample-requests/" + code.toLowerCase() + "-sample-inquiry.json";
            Map<String, Object> sampleRequest = readSample(objectMapper, fileName);
            @SuppressWarnings("unchecked")
            Map<String, Object> header = (Map<String, Object>) sampleRequest.get("header");
            loaded.add(new BusinessModuleInfo(
                    code,
                    definition.name(),
                    definition.group(),
                    "/" + code.toLowerCase(),
                    definition.localPort(),
                    String.valueOf(header.get("serviceId")),
                    String.valueOf(header.get("transactionCode")),
                    sampleRequest
            ));
        }
        return List.copyOf(loaded);
    }

    private Map<String, Object> readSample(ObjectMapper objectMapper, String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("샘플 전문을 읽을 수 없습니다: " + fileName, e);
        }
    }

    private record ModuleDefinition(String code, String name, String group, int localPort) {
    }
}
