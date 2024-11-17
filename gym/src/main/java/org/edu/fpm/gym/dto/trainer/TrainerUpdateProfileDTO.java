package org.edu.fpm.gym.dto.trainer;

import org.edu.fpm.gym.entity.TrainingType;

public record TrainerUpdateProfileDTO(String username, String firstName, String lastName,
                                      TrainingType trainingType, boolean isActive) {}
