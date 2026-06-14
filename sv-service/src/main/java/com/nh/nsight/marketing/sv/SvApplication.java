package com.nh.nsight.marketing.sv;

import org.mybatis.spring.annotation.MapperScan;
import com.nh.nsight.marketing.common.boot.LocalBootRun;
import com.nh.nsight.marketing.common.boot.NsightBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.sv.mapper")
public class SvApplication extends NsightBootApplication {
    @Override
    protected Class<?> primarySource() {
        return SvApplication.class;
    }

    public static void main(String[] args) {
        LocalBootRun.apply(8085);
        System.out.println("====================================================================[SvApplication.main] start");
        System.out.println("args: " + java.util.Arrays.toString(args));
        SpringApplication.run(SvApplication.class, args);
        System.out.println("====================================================================[SvApplication.main] end");
    }
}
