package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TraineeService {

    private TraineeRepository traineeRepository;

    @Autowired
    public void setTraineeRepository(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }

    public Trainee getTraineeByUsername(String username) {
        return traineeRepository.findTraineeByUser_Username(username);
    }

    public void updateTraineeProfile(Trainee trainee) {
        traineeRepository.updateTraineeByUserUsername(trainee);
    }

    public void deleteTraineeByUsername(String username) {
        traineeRepository.deleteTraineeByUser_Username(username);
    }

    public void changeTraineePassword(String username, String newPassword) {
        traineeRepository.changePassword(username, newPassword);
    }

    public void switchTraineeActivation(String username) {
        traineeRepository.switchActivation(username);
    }

    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        return traineeRepository.findTrainingsByTraineeAndDateRange(username, fromDate, toDate, trainerName, trainingType);
    }
}
