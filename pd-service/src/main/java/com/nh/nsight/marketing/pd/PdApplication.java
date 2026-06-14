package com.nh.nsight.marketing.pd;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.pd.mapper")
public class PdApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return PdApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8086);
        SpringApplication.run(PdApplication.class, args);
    }
}
