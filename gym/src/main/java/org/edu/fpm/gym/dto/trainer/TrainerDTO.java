package org.edu.fpm.gym.dto.trainer;

import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.TrainingType;

public record TrainerDTO(UserDTO user, TrainingType specialization) {}
