package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findTrainerByUser_Username(username);
    }

    public void updateTrainerProfile(Trainer trainer) {
        trainerRepository.updateTrainerByUserUsername(trainer);
    }

    public void changeTrainerPassword(String username, String newPassword) {
        trainerRepository.changePassword(username, newPassword);
    }

    public void switchTrainerActivation(String username) {
        trainerRepository.switchActivation(username);
    }

    public void deleteTrainer(String username) {
        trainerRepository.deleteTrainerByUser_Username(username);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String traineeUsername) {
        return trainerRepository.findAvailableTrainersForTrainee(traineeUsername);
    }

    public List<Training> getTrainingsForTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return trainerRepository.getTrainingsForTrainer(username, fromDate, toDate, traineeName);
    }
}
