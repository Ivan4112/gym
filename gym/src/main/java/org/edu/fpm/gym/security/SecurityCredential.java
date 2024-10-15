package org.edu.fpm.gym.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.edu.fpm.gym.storage.TraineeStorage;
import org.edu.fpm.gym.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityCredential {

    TraineeStorage traineeStorage;
    TrainerStorage trainerStorage;

    @Autowired
    public void setTraineeStorage(TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    @Autowired
    public void setTrainerStorage(TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public String generateUsername(String firstName, String lastName, Long id) {
        var existsTrainee = traineeStorage.getTrainees().values().stream()
                .anyMatch(trainee -> trainee.getFirstName().equals(firstName) && trainee.getLastName().equals(lastName));

        var existsTrainer = trainerStorage.getTrainers().values().stream()
                .anyMatch(trainee -> trainee.getFirstName().equals(firstName) && trainee.getLastName().equals(lastName));

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
