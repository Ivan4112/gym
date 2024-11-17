package org.edu.fpm.gym.dto.trainee;

import org.edu.fpm.gym.dto.trainer.TrainerDTO;

import java.util.List;

public record TraineeProfileDTO(
        String firstName,
        String lastName,
        String dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerDTO> trainers) {
}
