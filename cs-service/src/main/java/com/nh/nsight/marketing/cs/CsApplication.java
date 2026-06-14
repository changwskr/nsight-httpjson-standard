package com.nh.nsight.marketing.cs;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.cs.mapper")
public class CsApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8093);
        SpringApplication.run(CsApplication.class, args);
    }
}
