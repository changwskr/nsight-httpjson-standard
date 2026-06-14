package com.nh.nsight.marketing.om;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.om.mapper")
public class OmApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8096);
        SpringApplication.run(OmApplication.class, args);
    }
}
