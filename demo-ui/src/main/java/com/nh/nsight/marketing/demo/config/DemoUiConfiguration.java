package com.nh.nsight.marketing.demo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DemoUiProperties.class)
public class DemoUiConfiguration {
}
