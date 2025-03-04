package org.edu.fpm.gym.service.messaging.dlq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeadLetterQueueListener {
    @JmsListener(destination = "workload-request-dlq")
    public void handleFailedWorkloadRequests(String message) {
        log.error("⚠ Failed workload request moved to DLQ: {}", message);
    }

    @JmsListener(destination = "workload-response-dlq")
    public void handleFailedWorkloadResponses(String message) {
        log.error("⚠ Failed workload response moved to DLQ: {}", message);
    }
}
