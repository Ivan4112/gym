package org.edu.fpm.gym.health;

import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TrainerServiceHealthIndicator implements HealthIndicator {
    private final TrainerService trainerService;

    @Autowired
    public TrainerServiceHealthIndicator(TrainerService trainerService) {
        this.trainerService = trainerService;
    }


    @Override
    public Health health() {
        try {
            trainerService.getTrainerByUsername("trainerUsername");
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
