package com.nh.nsight.marketing.ss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ss.mapper")
public class SsApplication {
    public static void main(String[] args) {
        SpringApplication.run(SsApplication.class, args);
    }
}
