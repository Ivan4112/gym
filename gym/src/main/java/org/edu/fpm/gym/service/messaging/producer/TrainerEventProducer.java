package org.edu.fpm.gym.service.messaging.producer;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainerEventProducer {
    private final JmsTemplate jmsTemplate;

    public TrainerEventProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void requestTrainerWorkload(String username) {
        jmsTemplate.convertAndSend("workload-request-statistics", username);
    }
}
