package com.nh.nsight.marketing.etc;

import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.nh.nsight.marketing.common",
        "com.nh.nsight.marketing.etc"
})
@MapperScan("com.nh.nsight.marketing.etc.mapper")
public class EtcApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8098, "ET", "nsight-etc-service");
        SpringApplication.run(EtcApplication.class, args);
    }
}
