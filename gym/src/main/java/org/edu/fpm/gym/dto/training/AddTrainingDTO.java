package org.edu.fpm.gym.dto.training;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record AddTrainingDTO(String traineeUsername, String trainerUsername,
                             String trainingName,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate trainingDate,
                             int trainingDuration) { }
