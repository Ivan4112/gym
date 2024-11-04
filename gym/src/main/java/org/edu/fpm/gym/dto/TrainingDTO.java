package org.edu.fpm.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.edu.fpm.gym.entity.TrainingType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainingDTO {
    private String trainingName;
    private LocalDate trainingDate;
    private TrainingType trainingType;
    private Integer trainingDuration;
    private String userName;
}
