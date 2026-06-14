package com.nh.nsight.marketing.cc;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.cc.mapper")
public class CcApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return CcApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8080);
        SpringApplication.run(CcApplication.class, args);
    }
}
