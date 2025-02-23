package org.edu.fpm.gym.dto.training;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import org.edu.fpm.gym.entity.TrainingType;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TrainingRequestDTO(@NotEmpty String username, LocalDate periodFrom,
                                 LocalDate periodTo, String trainerName,
                                 TrainingType trainingType, @NotEmpty String password) {
}
