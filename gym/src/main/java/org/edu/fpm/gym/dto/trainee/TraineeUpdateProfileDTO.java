package org.edu.fpm.gym.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;


public record TraineeUpdateProfileDTO(String username, String firstName, String lastName,
                                      @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,
                                      String address, boolean isActive) {}
