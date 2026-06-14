package com.nh.nsight.marketing.ep;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ep.mapper")
public class EpApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return EpApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8089);
        SpringApplication.run(EpApplication.class, args);
    }
}
