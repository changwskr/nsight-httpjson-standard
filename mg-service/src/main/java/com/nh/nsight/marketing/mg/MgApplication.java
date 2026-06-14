package com.nh.nsight.marketing.mg;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.mg.mapper")
public class MgApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return MgApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8095);
        SpringApplication.run(MgApplication.class, args);
    }
}
