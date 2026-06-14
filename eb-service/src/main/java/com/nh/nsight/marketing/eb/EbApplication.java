package com.nh.nsight.marketing.eb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.eb.mapper")
public class EbApplication {
    public static void main(String[] args) {
        SpringApplication.run(EbApplication.class, args);
    }
}
