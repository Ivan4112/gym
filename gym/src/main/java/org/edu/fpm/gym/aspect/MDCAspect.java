package org.edu.fpm.gym.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
@Slf4j
public class MDCAspect {
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public Object logTransactionId(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = MDC.get("transactionId");
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);
        }
        try {
            log.info("Transaction {} started for method: {}", transactionId, joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            log.info("Transaction {} ended for method: {}", transactionId, joinPoint.getSignature().getName());
            MDC.clear();
        }
    }
}
