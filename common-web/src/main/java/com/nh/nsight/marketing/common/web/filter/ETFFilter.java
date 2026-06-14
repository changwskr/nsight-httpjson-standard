package com.nh.nsight.marketing.common.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ETFFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger("TRANSACTION_LOG");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println(
                "====================================================================[ETFFilter.doFilterInternal] start");
        System.out.println("uri: " + request.getRequestURI());
        System.out.println("method: " + request.getMethod());
        try {
            filterChain.doFilter(request, response);
        } finally {
            Long start = (Long) request.getAttribute(STFFilter.REQUEST_START_TIME);
            long elapsed = start == null ? -1 : System.currentTimeMillis() - start;
            String guid = String.valueOf(request.getAttribute(STFFilter.REQUEST_GUID));
            String traceId = String.valueOf(request.getAttribute(STFFilter.REQUEST_TRACE_ID));
            System.out.println("guid: " + guid);
            System.out.println("traceId: " + traceId);
            System.out.println("status: " + response.getStatus());
            System.out.println("elapsedMs: " + elapsed);
            log.info("ETF_END guid={} traceId={} uri={} status={} elapsedMs={}", guid, traceId,
                    request.getRequestURI(), response.getStatus(), elapsed);
            MDC.clear();
            System.out.println(
                    "====================================================================[ETFFilter.doFilterInternal] end");
        }
    }
}
