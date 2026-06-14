package com.nh.nsight.marketing.common.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/** Supports external Tomcat WAR deployment. */
public abstract class NsightBootApplication extends SpringBootServletInitializer {

    protected abstract Class<?> primarySource();

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(primarySource());
    }
}
