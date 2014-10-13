package com.github.yasbc.filters;

import com.github.yasbc.utils.Auditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class Logging extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(Logging.class);

    @SuppressWarnings({"PMD.AvoidCatchingThrowable", "PMD.DataflowAnomalyAnalysis"})
    public void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain) throws ServletException, IOException {
        final long start = System.currentTimeMillis();

        MDC.put("ipAddress", req.getRemoteAddr());
        MDC.put("hostname", req.getServerName());
        MDC.put("locale", req.getLocale().getDisplayName());
        MDC.put("uri", req.getRequestURI());
        MDC.put("query", req.getQueryString());
        MDC.put("params", req.getParameterMap().toString());
        MDC.put("method", req.getMethod());
        MDC.put("type", req.getContentType());

        final HttpSession session = req.getSession(false);
        if (session != null) {
            MDC.put("sessionId", session.getId());

            final String userId = (String) session.getAttribute("userId");
            if (userId != null) {
                MDC.put("userId", userId);
            }
        }

        Boolean error = false;
        try {
            chain.doFilter(req, res);
        } catch (final Throwable t) {
            error = true;

            final StringBuilder out = new StringBuilder("\t----------\n\tUNHANDLED EXCEPTION CAUGHT! details:\n");

            final Throwable root = Auditor.findRootCause(t);

            if (t.getMessage() != null) {
                out.append("\t--> Message: ").append(root.getMessage()).append('\n');
            }
            if (t.getStackTrace() != null) {
                out.append("\t--> Trace:   ").append(Arrays.toString(t.getStackTrace()).replaceAll(", ", "\n\t")).append('\n');
            }

            Auditor.exception(out.toString());
        }

        String auditEntry = Auditor.flush(true);

        if (auditEntry != null) {
            MDC.put("duration", System.currentTimeMillis() - start + " ms");

            auditEntry = new StringBuilder(MDC.getCopyOfContextMap().toString()).append("\n").append(auditEntry).toString();
            if (error) {
                LOGGER.error(auditEntry);
            } else {
                LOGGER.info(auditEntry);
            }
        }

        MDC.clear();
    }
}