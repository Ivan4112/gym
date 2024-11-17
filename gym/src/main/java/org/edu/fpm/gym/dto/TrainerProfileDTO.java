package org.edu.fpm.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.edu.fpm.gym.entity.TrainingType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerProfileDTO {
    private String firstName;
    private String lastName;
    private TrainingType trainingType;
    private boolean isActive;
    private List<TraineeForTrainerDTO> trainees;
}
