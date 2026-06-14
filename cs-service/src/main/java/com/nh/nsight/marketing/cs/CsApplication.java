package com.nh.nsight.marketing.cs;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.cs.mapper")
public class CsApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return CsApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8093);
        SpringApplication.run(CsApplication.class, args);
    }
}
