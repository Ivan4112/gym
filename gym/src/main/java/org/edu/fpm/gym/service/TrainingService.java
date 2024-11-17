package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    @Autowired
    public TrainingService(TrainingRepository trainingRepository,
                           TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository,
                           TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public boolean addTraining(String traineeUsername, String trainerUsername,
                               String trainingName, LocalDate trainingDate, int trainingDuration) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Adding training: {} for trainee: {} with trainer: {}", trainingName, traineeUsername, trainerUsername);

        Trainee trainee = traineeRepository.findTraineeByUser_Username(traineeUsername);
        Trainer trainer = trainerRepository.findTrainerByUser_Username(trainerUsername);
        TrainingType trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingName);

        Training training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(trainingName);
        training.setTrainingDate(trainingDate);
        training.setTrainingDuration(trainingDuration);
        training.setTrainingType(trainingType);

        trainingRepository.save(training);
        log.info("Training successfully added: {}", training.getTrainingName());
        MDC.clear();
        return true;
    }

    public List<Training> getTrainingsByTrainee(Long traineeId) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching trainings for trainee with ID: {}", traineeId);

        List<Training> trainingList = trainingRepository.findByTraineeId(traineeId);

        log.info("Found {} trainings for trainee with ID: {}", trainingList.size(), traineeId);
        MDC.clear();

        return trainingList;
    }

    public List<Training> getTrainingsByTrainer(Long trainerId) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        List<Training> trainingList = trainingRepository.findByTrainerId(trainerId);
        log.info("Found {} trainings for trainer with ID: {}", trainingList.size(), trainerId);
        MDC.clear();

        return trainingList;
    }
}
