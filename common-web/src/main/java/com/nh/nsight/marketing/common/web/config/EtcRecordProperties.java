package com.nh.nsight.marketing.common.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nsight.etc")
public class EtcRecordProperties {
    private boolean recordEnabled = false;
    private String recordUrl = "http://localhost:8098/et/transaction-io/record";

    public boolean isRecordEnabled() {
        return recordEnabled;
    }

    public void setRecordEnabled(boolean recordEnabled) {
        this.recordEnabled = recordEnabled;
    }

    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
}
