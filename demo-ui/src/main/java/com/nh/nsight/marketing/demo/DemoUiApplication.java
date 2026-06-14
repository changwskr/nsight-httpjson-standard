package com.nh.nsight.marketing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoUiApplication {
    public static void main(String[] args) {
        // IDE 멀티모듈 classpath에서 다른 업무 application.yml(8085 등)이 섞이는 경우를 방지
        System.setProperty("server.port", "8099");
        SpringApplication.run(DemoUiApplication.class, args);
    }
}
