package org.edu.fpm.gym.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityCredential {

    TraineeService traineeService;
    TrainerService trainerService;

    @Autowired
    public SecurityCredential(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    public String generateUsername(String firstName, String lastName, Integer id) {
        var existsTrainee = traineeService.existsByFullName(firstName, lastName);

        var existsTrainer = trainerService.existsByFullName(firstName, lastName);

        if (existsTrainee || existsTrainer) {
            return firstName + "." + lastName + id;
        } else {
            return firstName + "." + lastName;
        }
    }

    public String generatePassword() {
        int passwordLength = 10;
        return RandomStringUtils.randomAlphanumeric(passwordLength);
    }
}
