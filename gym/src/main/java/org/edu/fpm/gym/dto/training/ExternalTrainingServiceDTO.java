package org.edu.fpm.gym.dto.training;

import java.time.LocalDate;

public record ExternalTrainingServiceDTO(
        String trainerUsername, String trainerFirstName,
        String trainerLastName, boolean isActive,
        LocalDate trainingDate, int trainingDuration, String actionType) {}
