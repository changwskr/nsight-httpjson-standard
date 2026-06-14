package com.nh.nsight.marketing.ss;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.ss.mapper")
public class SsApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return SsApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8092);
        SpringApplication.run(SsApplication.class, args);
    }
}
