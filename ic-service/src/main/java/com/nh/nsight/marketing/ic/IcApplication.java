package com.nh.nsight.marketing.ic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ic.mapper")
public class IcApplication {
    public static void main(String[] args) {
        SpringApplication.run(IcApplication.class, args);
    }
}
