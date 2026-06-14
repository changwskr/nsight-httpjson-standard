package com.nh.nsight.marketing.common.boot;

public final class LocalBootRun {
    private LocalBootRun() {
    }

    /**
     * IDE 멀티모듈 실행 시 application.yml이 classpath에 없을 때 bootRun 포트/프로필을 맞춘다.
     */
    public static void apply(int port) {
        apply(port, null, null);
    }

    /**
     * 공통 모듈(common-etc, common-updownload) IDE 실행 시 타 업무 모듈 설정이 섞이는 것을 방지한다.
     */
    public static void apply(int port, String businessCode, String applicationName) {
        System.setProperty("server.port", String.valueOf(port));
        System.setProperty("spring.profiles.active", "local");
        if (businessCode != null && !businessCode.isBlank()) {
            System.setProperty("nsight.module.business-code", businessCode);
        }
        if (applicationName != null && !applicationName.isBlank()) {
            System.setProperty("spring.application.name", applicationName);
        }
    }
}
