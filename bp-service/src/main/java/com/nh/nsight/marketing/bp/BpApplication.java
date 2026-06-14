package com.nh.nsight.marketing.bp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.bp.mapper")
public class BpApplication {
    public static void main(String[] args) {
        SpringApplication.run(BpApplication.class, args);
    }
}
