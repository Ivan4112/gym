package org.edu.fpm.gym.service.messaging.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.externalservice.TrainerWorkloadSummaryDTO;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@Service
public class TrainerEventListener {
    private final ConcurrentMap<String, CompletableFuture<TrainerWorkloadSummaryDTO>> responseMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;
    private final JmsTemplate jmsTemplate;

    public TrainerEventListener(ObjectMapper objectMapper, JmsTemplate jmsTemplate) {
        this.objectMapper = objectMapper;
        this.jmsTemplate = jmsTemplate;
    }

    @JmsListener(destination = "workload-response-statistics")
    public void receiveWorkloadStatisticsEvent(String receiveStatisticsFromExternalService) {
        try {
            TrainerWorkloadSummaryDTO summary = objectMapper.readValue(receiveStatisticsFromExternalService, TrainerWorkloadSummaryDTO.class);
            log.info("Receiving workload statistics for trainer {}", summary.firstName());
            if (summary.username() != null && responseMap.containsKey(summary.username())) {
                responseMap.get(summary.username()).complete(summary);
            }
        }catch (Exception e) {
            sendToDeadLetterQueue(receiveStatisticsFromExternalService, "Deserialization error");
        }
    }

    public CompletableFuture<TrainerWorkloadSummaryDTO> getFutureResponse(String username) {
        CompletableFuture<TrainerWorkloadSummaryDTO> future = new CompletableFuture<>();
        responseMap.put(username, future);
        return future;
    }

    private void sendToDeadLetterQueue(String message, String reason) {
        try {
            jmsTemplate.convertAndSend("workload-response-dlq", message);
            log.error("Moved to DLQ (workload-response-dlq): {}", reason);
        } catch (Exception e) {
            log.error("Failed to send message to DLQ: {}", e.getMessage());
        }
    }
}
