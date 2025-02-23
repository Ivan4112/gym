package org.edu.fpm.gym.config;

import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            String transactionId = MDC.get("transactionId");
            if (transactionId != null) {
                requestTemplate.header("X-Transaction-Id", transactionId);
            }
        };
    }
}
