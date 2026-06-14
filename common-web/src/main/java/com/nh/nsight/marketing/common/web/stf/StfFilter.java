package com.nh.nsight.marketing.common.web.stf;

import com.nh.nsight.marketing.common.util.GuidUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StfFilter extends OncePerRequestFilter {
    public static final String REQUEST_START_TIME = "NSIGHT_REQUEST_START_TIME";
    public static final String REQUEST_GUID = "NSIGHT_REQUEST_GUID";
    public static final String REQUEST_TRACE_ID = "NSIGHT_REQUEST_TRACE_ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String guid = headerOrNew(request, "X-GUID", GuidUtil.newGuid());
        String traceId = headerOrNew(request, "X-Trace-Id", GuidUtil.newTraceId());

        request.setAttribute(REQUEST_START_TIME, startTime);
        request.setAttribute(REQUEST_GUID, guid);
        request.setAttribute(REQUEST_TRACE_ID, traceId);

        MDC.put("guid", guid);
        MDC.put("traceId", traceId);
        try {
            response.setHeader("X-GUID", guid);
            response.setHeader("X-Trace-Id", traceId);
            filterChain.doFilter(request, response);
        } finally {
            // MDC 정리는 ETF에서 수행한다. ETF 미등록 상황에 대비해 여기서도 방어적으로 제거한다.
            MDC.remove("guid");
            MDC.remove("traceId");
        }
    }

    private String headerOrNew(HttpServletRequest request, String name, String generated) {
        String value = request.getHeader(name);
        if (value == null || value.isBlank()) {
            return generated;
        }
        return value;
    }
}
