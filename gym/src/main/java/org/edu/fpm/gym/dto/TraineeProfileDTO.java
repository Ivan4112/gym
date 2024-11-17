package org.edu.fpm.gym.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TraineeProfileDTO {
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDTO> trainers;
}
