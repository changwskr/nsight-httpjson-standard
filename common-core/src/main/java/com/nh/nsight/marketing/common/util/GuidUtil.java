package com.nh.nsight.marketing.common.util;

import java.util.UUID;

public final class GuidUtil {
    private GuidUtil() {}

    public static String newGuid() {
        return UUID.randomUUID().toString();
    }

    public static String newTraceId() {
        return "trc-" + UUID.randomUUID();
    }
}
