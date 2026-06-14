package com.nh.nsight.marketing.common.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({BusinessModuleProperties.class, EtcRecordProperties.class})
public class NsightWebConfiguration {
}
