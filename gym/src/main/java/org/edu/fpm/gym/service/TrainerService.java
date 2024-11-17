package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.*;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TrainerService {

    private final TrainerRepository trainerRepository;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public Trainer createTrainer(Trainer trainer) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Creating trainer: {}", trainer.getUser().getUsername());

        Trainer createdTrainer = trainerRepository.save(trainer);

        log.info("Trainer created successfully: {}", createdTrainer.getUser().getUsername());
        MDC.clear();
        return createdTrainer;
    }

    public Trainer getTrainerByUsername(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching trainer with username: {}", username);

        Trainer trainer = trainerRepository.findTrainerByUser_Username(username);

        if (trainer != null) {
            log.info("Trainer found with username: {}", username);
        } else {
            log.warn("Trainer not found with username: {}", username);
        }

        MDC.clear();
        return trainer;
    }

    public void switchTrainerActivation(String username, boolean isActive) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Switching activation for trainer: {} to {}", username, isActive);

        trainerRepository.switchActivation(username, isActive);

        log.info("Trainer activation switched: {} -> {}", username, isActive);
        MDC.clear();
    }

    public void deleteTrainer(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Deleting trainer with username: {}", username);

        trainerRepository.deleteTrainerByUser_Username(username);

        log.info("Trainer deleted: {}", username);
        MDC.clear();
    }

    public List<Trainer> getAvailableTrainersForTrainee(String traineeUsername) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching available trainers for trainee: {}", traineeUsername);

        List<Trainer> trainers = trainerRepository.findAvailableTrainersForTrainee(traineeUsername);

        log.info("Found {} available trainers for trainee: {}", trainers.size(), traineeUsername);
        MDC.clear();
        return trainers;
    }

    public List<TrainingDTO> getTrainingsForTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching trainings for trainer: {} between {} and {}", username, fromDate, toDate);

        List<Training> trainingList = trainerRepository.getTrainingsForTrainer(username, fromDate, toDate, traineeName);
        log.info("Found {} trainings for trainer: {}", trainingList.size(), username);
        MDC.clear();

        return trainingList.stream().map(
                training -> new TrainingDTO(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType(),
                        training.getTrainingDuration(),
                        training.getTrainee().getUser().getFirstName())
        ).collect(Collectors.toList());
    }

    public TrainerProfileDTO getTrainerProfile(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching trainer profile with username: {}", username);

        Trainer trainer = getTrainerByUsername(username);

        if (trainer == null) {
            throw new RuntimeException("Trainee not found");
        }

        log.info("Fetched profile for trainer: {}", username);
        MDC.clear();

        return getTrainerProfileDTO(trainer);
    }

    public TrainerProfileDTO updateTrainerProfile(TrainerUpdateProfileDTO updateProfile) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Updating trainer profile: {}", updateProfile.getUsername());

        Trainer trainer = getTrainerByUsername(updateProfile.getUsername());

        if (trainer == null) {
            log.warn("Trainer not found: {}", updateProfile.getUsername());
            throw new RuntimeException("Trainer not found");
        } else {
            User user = trainer.getUser();
            user.setFirstName(updateProfile.getFirstName());
            user.setLastName(updateProfile.getLastName());
            user.setUsername(updateProfile.getUsername());
            user.setIsActive(updateProfile.isActive());

            trainer.setSpecialization(updateProfile.getTrainingType());
            trainer.setUser(user);
            trainerRepository.updateTrainerByUserUsername(trainer.getUser().getUsername(), trainer);
            log.info("Trainer profile updated: {}", updateProfile.getUsername());
        }
        MDC.clear();
        return getTrainerProfileDTO(trainer);
    }

    private TrainerProfileDTO getTrainerProfileDTO(Trainer trainer) {
        List<TraineeForTrainerDTO> trainees = trainer.getTrainees().stream()
                .map(trainee -> new TraineeForTrainerDTO(
                        trainee.getUser().getFirstName(),
                        trainee.getUser().getLastName(),
                        trainee.getUser().getUsername()
                ))
                .collect(Collectors.toList());

        return new TrainerProfileDTO(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getSpecialization(),
                trainer.getUser().getIsActive(),
                trainees
        );
    }
}
