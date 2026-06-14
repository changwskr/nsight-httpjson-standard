package com.nh.nsight.marketing.ct;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ct.mapper")
public class CtApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return CtApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8094);
        SpringApplication.run(CtApplication.class, args);
    }
}
