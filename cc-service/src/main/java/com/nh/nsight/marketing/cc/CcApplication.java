package com.nh.nsight.marketing.cc;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.cc.mapper")
public class CcApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8080);
        SpringApplication.run(CcApplication.class, args);
    }
}
