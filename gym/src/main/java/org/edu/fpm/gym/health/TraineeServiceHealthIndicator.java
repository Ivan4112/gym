package org.edu.fpm.gym.health;

import org.edu.fpm.gym.service.TraineeService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TraineeServiceHealthIndicator implements HealthIndicator {
    private final TraineeService traineeService;

    public TraineeServiceHealthIndicator(TraineeService service) {
        this.traineeService = service;
    }

    @Override
    public Health health() {
        try {
            traineeService.getTraineeByUsername("testUser");
            return Health.up()
                    .withDetail("TraineeService", "Operational")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("TraineeService", "Service is unavailable")
                    .withDetail("Error", e.getMessage())
                    .build();
        }
    }
}
