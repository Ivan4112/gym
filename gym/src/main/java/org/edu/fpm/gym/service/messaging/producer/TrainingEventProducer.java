package org.edu.fpm.gym.service.messaging.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainingEventProducer {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    public TrainingEventProducer(JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendTrainingUpdateEvent(ExternalTrainingServiceDTO training) {
        try {
            String jsonTrainingMessage = objectMapper.writeValueAsString(training);
            jmsTemplate.convertAndSend("trainer-workload-queue", jsonTrainingMessage);
        } catch (JsonProcessingException e) {
            sendToDeadLetterQueue(training, "JSON serialization error");
        } catch (IllegalArgumentException e) {
            sendToDeadLetterQueue(training, "Missing required fields");
        } catch (JmsException e) {
            sendToDeadLetterQueue(training, "JMS error while sending message");
        }
    }

    private void sendToDeadLetterQueue(ExternalTrainingServiceDTO training, String reason) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(training);
            jmsTemplate.convertAndSend("trainer-workload-dlq", jsonMessage);
            System.err.println("Message moved to DLQ: " + reason);
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize message for DLQ: " + e.getMessage());
        }
    }
}
