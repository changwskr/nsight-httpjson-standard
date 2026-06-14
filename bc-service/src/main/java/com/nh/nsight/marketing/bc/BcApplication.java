package com.nh.nsight.marketing.bc;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.bc.mapper")
public class BcApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8083);
        SpringApplication.run(BcApplication.class, args);
    }
}
