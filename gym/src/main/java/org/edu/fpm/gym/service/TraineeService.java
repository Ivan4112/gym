package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.*;
import org.edu.fpm.gym.entity.*;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TrainerService trainerService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository, TrainerService trainerService) {
        this.traineeRepository = traineeRepository;
        this.trainerService = trainerService;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public Trainee createTrainee(Trainee trainee) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Transaction started for creating trainee with username: {}", trainee.getUser().getUsername());
        try {
            Trainee savedTrainee = traineeRepository.save(trainee);
            log.info("Transaction {}: Trainee created with username: {}", transactionId, savedTrainee.getUser().getUsername());
            return savedTrainee;
        } finally {
            MDC.clear();
        }
    }

    public Trainee getTraineeByUsername(String username) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Transaction started for fetching trainee with username: {}", username);
        try {
            Trainee trainee = traineeRepository.findTraineeByUser_Username(username);
            if (trainee != null) {
                log.info("Transaction {}: Found trainee with username: {}", transactionId, username);
            } else {
                log.info("Transaction {}: Trainee not found with username: {}", transactionId, username);
            }
            return trainee;
        } finally {
            MDC.clear();
        }
    }

    public void deleteTraineeByUsername(String username) {
        String transactionId = generateTransactionId();
        MDC.put("deleteTransactionId", transactionId);
        log.info("Transaction started for deleting trainee with username: {}", username);
        try {
            Trainee trainee = traineeRepository.findTraineeByUser_Username(username);
            if (trainee != null) {
                log.info("Transaction {}: Trainee found with username: {}", transactionId, username);
                traineeRepository.delete(trainee);
                log.info("Transaction {}: Trainee deleted with username: {}", transactionId, username);
            } else {
                log.info("Transaction {}: Trainee not found with username: {}", transactionId, username);
            }
        } finally {
            MDC.clear();
        }
    }

    public void switchTraineeActivation(String username, boolean isActive) {
        String transactionId = generateTransactionId();
        MDC.put("switchTransactionId", transactionId);
        log.info("Transaction started for switching activation status of trainee with username: {}", username);
        try {
            traineeRepository.switchActivation(username, isActive);
            log.info("Transaction {}: Activation status for trainee with username: {} set to {}", transactionId, username, isActive);
        } finally {
            MDC.clear();
        }
    }

    public List<TrainingDTO> getTraineeTrainings(String username, LocalDate fromDate,
                                                 LocalDate toDate, String trainerName,
                                                 TrainingType trainingType) {
        String transactionId = generateTransactionId();
        MDC.put("getTraineeTransactionId", transactionId);
        log.info("Transaction started for fetching trainings for trainee with username: {}", username);

        List<Training> trainings = traineeRepository.findTrainingsByTraineeAndDateRange(username, fromDate, toDate, trainerName, trainingType);

        log.info("Transaction {}: Found {} trainings for trainee with username: {}", transactionId, trainings.size(), username);
        MDC.clear();
        return trainings.stream().map(
                training -> new TrainingDTO(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName())
        ).collect(Collectors.toList());
    }

    public List<TrainerDTO> updateTraineeTrainers(String username, List<String> trainerUsernames) {
        String transactionId = generateTransactionId();
        MDC.put("updateTransactionId", transactionId);
        log.info("Transaction started for updating trainers for trainee with username: {}", username);

        Trainee trainee = getTraineeByUsername(username);
        if (trainee == null) {
            throw new RuntimeException("Trainee not found");
        } else {

            Set<Trainer> trainers = trainerUsernames.stream()
                    .map(trainerService::getTrainerByUsername)
                    .collect(Collectors.toSet());

            trainee.setTrainers(trainers);
            traineeRepository.save(trainee);

            log.info("Transaction {}: Trainers updated for trainee with username: {}", transactionId, username);
            MDC.clear();
            return trainers.stream().map(
                    trainer -> new TrainerDTO(
                            new UserDTO(
                                    trainer.getUser().getUsername(),
                                    trainer.getUser().getFirstName(),
                                    trainer.getUser().getLastName(),
                                    trainer.getUser().getIsActive()),
                            trainer.getSpecialization())
            ).collect(Collectors.toList());
        }
    }

    public TraineeProfileDTO getTraineeProfile(String username) {
        Trainee trainee = getTraineeByUsername(username);

        if (trainee == null) {
            throw new RuntimeException("Trainee not found");
        }

        return getTraineeProfileDTO(trainee);
    }


    public TraineeProfileDTO updateTraineeProfile(TraineeUpdateProfileDTO updateProfile) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Transaction started for updating profile of trainee with username: {}", updateProfile.getUsername());

        Trainee trainee = getTraineeByUsername(updateProfile.getUsername());

        if (trainee == null) {
            throw new RuntimeException("Trainee not found");
        } else {
            User user = trainee.getUser();
            user.setFirstName(updateProfile.getFirstName());
            user.setLastName(updateProfile.getLastName());
            user.setUsername(updateProfile.getUsername());
            user.setIsActive(updateProfile.isActive());

            trainee.setDateOfBirth(updateProfile.getDateOfBirth());
            trainee.setAddress(updateProfile.getAddress());
            trainee.setUser(user);
            traineeRepository.updateTraineeByUserUsername(trainee.getUser().getUsername(), trainee);
            log.info("Transaction {}: Updated profile for trainee with username: {}", transactionId, updateProfile.getUsername());
        }
        MDC.clear();
        return getTraineeProfileDTO(trainee);
    }

    private TraineeProfileDTO getTraineeProfileDTO(Trainee trainee) {
        List<TrainerDTO> trainers = trainee.getTrainers().stream()
                .map(trainer -> new TrainerDTO(
                        new UserDTO(
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getUser().getUsername(),
                                trainer.getUser().getIsActive()
                        ),
                        trainer.getSpecialization()
                ))
                .collect(Collectors.toList());

        return new TraineeProfileDTO(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName(),
                trainee.getDateOfBirth().toString(),
                trainee.getAddress(),
                trainee.getUser().getIsActive(),
                trainers
        );
    }
}
