package org.edu.fpm.gym.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.security.SecurityCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TraineeService {

    private TraineeRepository traineeRepository;

    private SecurityCredential securityCredential;

    @Autowired
    public void setSecurityCredential(SecurityCredential securityCredential) {
        this.securityCredential = securityCredential;
    }
    @Autowired
    public void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public Trainee createTrainee(Trainee trainee) {
        trainee.getUser().setUsername(securityCredential.generateUsername(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getUser().getId())
        );
        trainee.getUser().setPassword(securityCredential.generatePassword());
        return traineeRepository.save(trainee);
    }

    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.findByUsername(username);
    }

    public void updateTraineeProfile(Trainee trainee) {
        traineeRepository.updateTrainee(trainee);
    }

    public void deleteTraineeByUsername(String username) {
        traineeRepository.deleteByUsername(username);
    }

    public void changeTraineePassword(String username, String newPassword) {
        traineeRepository.changePassword(username, newPassword);
    }

    public void switchTraineeActivation(String username) {
        traineeRepository.switchActivation(username);
    }

    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        return traineeRepository.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    public boolean existsByFullName(String firstName, String lastName) {
        return traineeRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }
}
