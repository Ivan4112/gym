package org.edu.fpm.gym.dto.training;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.edu.fpm.gym.entity.TrainingType;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public record TrainingDTO(String trainingName, LocalDate trainingDate,
                          TrainingType trainingType, Integer trainingDuration, String userName) {}
