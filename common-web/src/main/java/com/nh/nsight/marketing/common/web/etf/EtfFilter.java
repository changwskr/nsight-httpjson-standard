package com.nh.nsight.marketing.common.web.etf;

import com.nh.nsight.marketing.common.web.stf.StfFilter;
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
public class EtfFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger("TRANSACTION_LOG");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            Long start = (Long) request.getAttribute(StfFilter.REQUEST_START_TIME);
            long elapsed = start == null ? -1 : System.currentTimeMillis() - start;
            String guid = String.valueOf(request.getAttribute(StfFilter.REQUEST_GUID));
            String traceId = String.valueOf(request.getAttribute(StfFilter.REQUEST_TRACE_ID));
            log.info("ETF_END guid={} traceId={} uri={} status={} elapsedMs={}", guid, traceId,
                    request.getRequestURI(), response.getStatus(), elapsed);
            MDC.clear();
        }
    }
}
