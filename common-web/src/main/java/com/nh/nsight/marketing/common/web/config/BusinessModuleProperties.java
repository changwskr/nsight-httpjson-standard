package com.nh.nsight.marketing.common.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nsight.module")
public class BusinessModuleProperties {
    private String systemId = "NSIGHT-MP";
    private String businessCode;
    private String apId = "local-ap";

    public String getSystemId() { return systemId; }
    public void setSystemId(String systemId) { this.systemId = systemId; }
    public String getBusinessCode() { return businessCode; }
    public void setBusinessCode(String businessCode) { this.businessCode = businessCode; }
    public String getApId() { return apId; }
    public void setApId(String apId) { this.apId = apId; }
}
