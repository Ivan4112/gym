package org.edu.fpm.gym.aspect;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String transactionId = MDC.get("transactionId");
        if (transactionId != null) {
            requestTemplate.header("X-Transaction-Id", transactionId);
        }
    }
}
