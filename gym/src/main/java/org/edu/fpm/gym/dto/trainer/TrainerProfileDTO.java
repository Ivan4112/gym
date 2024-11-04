package org.edu.fpm.gym.dto.trainer;

import org.edu.fpm.gym.entity.TrainingType;

import java.util.List;

public record TrainerProfileDTO(String firstName, String lastName, TrainingType trainingType,
                                boolean isActive, List<TraineeForTrainerDTO> trainees) { }
