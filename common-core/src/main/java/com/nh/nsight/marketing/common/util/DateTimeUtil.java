package com.nh.nsight.marketing.common.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter BASIC_DATE = DateTimeFormatter.BASIC_ISO_DATE;

    private DateTimeUtil() {}

    public static String nowKst() {
        return OffsetDateTime.now(KST).toString();
    }

    public static String todayKst() {
        return LocalDate.now(KST).format(BASIC_DATE);
    }
}
