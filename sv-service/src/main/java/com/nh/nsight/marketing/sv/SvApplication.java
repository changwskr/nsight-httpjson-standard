package com.nh.nsight.marketing.sv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.nh.nsight.marketing")
@MapperScan("com.nh.nsight.marketing.sv.mapper")
public class SvApplication {
    public static void main(String[] args) {
        System.out.println("====================================================================[SvApplication.main] start");
        System.out.println("args: " + java.util.Arrays.toString(args));
        SpringApplication.run(SvApplication.class, args);
        System.out.println("====================================================================[SvApplication.main] end");
    }
}
