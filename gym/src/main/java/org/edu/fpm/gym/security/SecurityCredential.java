package org.edu.fpm.gym.security;

import org.edu.fpm.gym.storage.TraineeStorage;
import org.edu.fpm.gym.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

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
        boolean existsTrainee = traineeStorage.getTrainees().values().stream()
                .anyMatch(trainee -> trainee.getFirstName().equals(firstName) && trainee.getLastName().equals(lastName));

        boolean existsTrainer = trainerStorage.getTrainers().values().stream()
                .anyMatch(trainee -> trainee.getFirstName().equals(firstName) && trainee.getLastName().equals(lastName));

        if (existsTrainee || existsTrainer) {
            return firstName + "." + lastName + id;
        } else {
            return firstName + "." + lastName;
        }
    }

    public String generatePassword() {
        int passwordLength = 10;
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            password.append(symbols.charAt(random.nextInt(symbols.length())));
        }

        return password.toString();
    }
}
