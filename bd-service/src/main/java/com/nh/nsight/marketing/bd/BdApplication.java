package com.nh.nsight.marketing.bd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.bd.mapper")
public class BdApplication {
    public static void main(String[] args) {
        SpringApplication.run(BdApplication.class, args);
    }
}
