package com.nh.nsight.marketing.cm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.cm.mapper")
public class CmApplication {
    public static void main(String[] args) {
        SpringApplication.run(CmApplication.class, args);
    }
}
