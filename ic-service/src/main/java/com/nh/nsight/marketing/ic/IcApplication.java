package com.nh.nsight.marketing.ic;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ic.mapper")
public class IcApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return IcApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8081);
        SpringApplication.run(IcApplication.class, args);
    }
}
