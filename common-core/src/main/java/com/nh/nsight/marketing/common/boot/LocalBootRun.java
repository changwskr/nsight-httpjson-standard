package com.nh.nsight.marketing.common.boot;

public final class LocalBootRun {
    private LocalBootRun() {
    }

    /**
     * IDE 멀티모듈 실행 시 application.yml이 classpath에 없을 때 bootRun 포트/프로필을 맞춘다.
     */
    public static void apply(int port) {
        System.setProperty("server.port", String.valueOf(port));
        System.setProperty("spring.profiles.active", "local");
    }
}
