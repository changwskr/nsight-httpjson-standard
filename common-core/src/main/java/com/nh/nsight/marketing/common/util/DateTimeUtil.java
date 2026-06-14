package com.nh.nsight.marketing.common.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public final class DateTimeUtil {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private DateTimeUtil() {}

    public static String nowKst() {
        return OffsetDateTime.now(KST).toString();
    }
}
