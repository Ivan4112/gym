package org.edu.fpm.gym.dto.training;

import org.edu.fpm.gym.entity.TrainingType;

import java.time.LocalDate;

public record TrainingDTO(String trainingName, LocalDate trainingDate,
                          TrainingType trainingType, Integer trainingDuration, String userName) {}
