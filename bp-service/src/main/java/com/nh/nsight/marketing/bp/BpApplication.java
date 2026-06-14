package com.nh.nsight.marketing.bp;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.bp.mapper")
public class BpApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return BpApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8090);
        SpringApplication.run(BpApplication.class, args);
    }
}
