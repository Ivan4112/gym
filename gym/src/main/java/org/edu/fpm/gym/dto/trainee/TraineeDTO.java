package org.edu.fpm.gym.dto.trainee;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.edu.fpm.gym.dto.user.UserDTO;

import java.time.LocalDate;

public record TraineeDTO(@JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, String address, UserDTO user) { }
