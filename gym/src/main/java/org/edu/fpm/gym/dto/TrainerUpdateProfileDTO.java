package org.edu.fpm.gym.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.edu.fpm.gym.entity.TrainingType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerUpdateProfileDTO {
    @NotNull
    private String username;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private TrainingType trainingType;
    @NotNull
    private boolean isActive;
}
