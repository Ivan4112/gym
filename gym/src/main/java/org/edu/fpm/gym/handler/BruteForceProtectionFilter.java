package org.edu.fpm.gym.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.edu.fpm.gym.service.LoginAttemptService;
import java.io.IOException;

@Component
public class BruteForceProtectionFilter extends GenericFilterBean {
    private final LoginAttemptService loginAttemptService;

    public BruteForceProtectionFilter(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String username = httpRequest.getParameter("username");

        if (username != null && loginAttemptService.isBlocked(username)) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "User is blocked due to multiple failed login attempts.");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
