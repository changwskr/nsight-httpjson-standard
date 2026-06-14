package com.nh.nsight.marketing.updownload;

import com.nh.nsight.marketing.common.boot.LocalBootRun;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.nh.nsight.marketing.common",
        "com.nh.nsight.marketing.updownload"
})
@MapperScan("com.nh.nsight.marketing.updownload.mapper")
public class UpdownloadApplication {
    public static void main(String[] args) {
        LocalBootRun.apply(8097, "UD", "nsight-updownload-service");
        SpringApplication.run(UpdownloadApplication.class, args);
    }
}
