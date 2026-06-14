package com.nh.nsight.marketing.pd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.pd.mapper")
public class PdApplication {
    public static void main(String[] args) {
        SpringApplication.run(PdApplication.class, args);
    }
}
