package com.nh.nsight.marketing.ep;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ep.mapper")
public class EpApplication {
    public static void main(String[] args) {
        SpringApplication.run(EpApplication.class, args);
    }
}
