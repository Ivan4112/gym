package org.edu.fpm.gym.aspect;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, 
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String transactionId = MDC.get("transactionId");
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);
        }
        try {
            httpResponse.setHeader("X-Transaction-Id", transactionId);
            log.info("TransactionId: {}, Incoming servletRequest: {} {}", transactionId,
                    httpRequest.getMethod(), httpRequest.getRequestURI());

            filterChain.doFilter(servletRequest, servletResponse);

            log.info("TransactionId: {}, Response status: {}", transactionId, httpResponse.getStatus());
        } finally {MDC.clear();}
    }
}
