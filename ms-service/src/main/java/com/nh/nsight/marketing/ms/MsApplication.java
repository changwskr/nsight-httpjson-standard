package com.nh.nsight.marketing.ms;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ms.mapper")
public class MsApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8084);
        SpringApplication.run(MsApplication.class, args);
    }
}
