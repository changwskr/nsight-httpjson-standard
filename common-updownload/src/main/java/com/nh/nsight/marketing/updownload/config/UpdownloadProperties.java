package com.nh.nsight.marketing.updownload.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "nsight.updownload")
public class UpdownloadProperties {
    private String storagePath = "./data/updownload";
    private long maxFileSizeMb = 50;

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public long getMaxFileSizeMb() {
        return maxFileSizeMb;
    }

    public void setMaxFileSizeMb(long maxFileSizeMb) {
        this.maxFileSizeMb = maxFileSizeMb;
    }

    public long getMaxFileSizeBytes() {
        return maxFileSizeMb * 1024L * 1024L;
    }
}
