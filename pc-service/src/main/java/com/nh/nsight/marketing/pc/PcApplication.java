package com.nh.nsight.marketing.pc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.pc.mapper")
public class PcApplication {
    public static void main(String[] args) {
        SpringApplication.run(PcApplication.class, args);
    }
}
