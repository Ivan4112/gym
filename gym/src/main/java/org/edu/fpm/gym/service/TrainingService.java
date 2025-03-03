package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.*;
import org.edu.fpm.gym.service.messaging.producer.TrainingEventProducer;
import org.edu.fpm.gym.utils.ActionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingEventProducer trainingEventProducer;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository,
                           TrainingEventProducer trainingEventProducer) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
        this.trainingEventProducer = trainingEventProducer;
    }

    public String addTraining(AddTrainingDTO addTrainingDTO) {
        log.info("Adding training: {} for trainee: {} with trainer: {}", addTrainingDTO.trainingName(),
                addTrainingDTO.traineeUsername(), addTrainingDTO.trainerUsername());

        var trainee = traineeRepository.findTraineeByUser_Username(addTrainingDTO.traineeUsername());
        var trainer = trainerRepository.findTrainerByUser_Username(addTrainingDTO.trainerUsername());
        var trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(addTrainingDTO.trainingName());

        var training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(addTrainingDTO.trainingName());
        training.setTrainingDate(addTrainingDTO.trainingDate());
        training.setTrainingDuration(addTrainingDTO.trainingDuration());
        training.setTrainingType(trainingType);

        trainingRepository.save(training);
        sendTrainingDataToExternalService(training, ActionType.ADD);
        return "Training added successfully";
    }

    public void deleteTraining(Integer trainingId) {
        Optional<Training> trainingOptional = trainingRepository.findById(trainingId);
        if (trainingOptional.isPresent()) {
            Training training = trainingOptional.get();
            LocalDate trainingDate = training.getTrainingDate();

            if (trainingDate.isBefore(LocalDate.now())) {
                String message = String.format("Cannot delete training with ID %d. It has already been completed on %s", trainingId, trainingDate);
                log.error(message);
                throw new IllegalStateException(message);
            }

            if (trainingDate.minusDays(1).isBefore(LocalDate.now())) {
                String message = String.format("Cannot delete training with ID %d. It can only be deleted at least 24 hours in advance.", trainingId);
                log.error(message);
                throw new IllegalStateException(message);
            }
            sendTrainingDataToExternalService(training, ActionType.DELETE);
            trainingRepository.deleteById(trainingId);
            log.info("Training with ID {} deleted successfully.", trainingId);
        } else {
            log.warn("Training with ID {} not found.", trainingId);
        }
    }

    public ResponseEntity<List<ExternalTrainingServiceDTO>> getTrainingsByTrainer(Integer trainerId) {
        List<Training> trainingList = trainingRepository.findByTrainerId(trainerId);
        log.info("Found {} trainings for trainer with ID: {}", trainingList.size(), trainerId);
        if (trainingList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ExternalTrainingServiceDTO> trainingDTOs = trainingList.stream().map(training -> new ExternalTrainingServiceDTO(
                training.getTrainer().getUser().getUsername(),
                training.getTrainer().getUser().getFirstName(),
                training.getTrainer().getUser().getLastName(),
                training.getTrainer().getUser().getIsActive(),
                training.getTrainingDate(),
                training.getTrainingDuration(),
                ActionType.ADD
        )).collect(Collectors.toList());

        try {
            for (ExternalTrainingServiceDTO training : trainingDTOs) {
                trainingEventProducer.sendTrainingUpdateEvent(training);
            }
            log.info("Sent data to external service");
        } catch (Exception e) {
            log.error("Failed to initialize TrainerWorkloadService", e);
        }

        return ResponseEntity.ok(trainingDTOs);
    }

    private void sendTrainingDataToExternalService(Training training, ActionType actionType) {
        try {
            ExternalTrainingServiceDTO request = new ExternalTrainingServiceDTO(
                    training.getTrainer().getUser().getUsername(),
                    training.getTrainer().getUser().getFirstName(),
                    training.getTrainer().getUser().getLastName(),
                    training.getTrainer().getUser().getIsActive(),
                    training.getTrainingDate(),
                    training.getTrainingDuration(),
                    actionType
            );
            log.info("Request -> {}", request);

            trainingEventProducer.sendTrainingUpdateEvent(request);
            log.info("Successfully sent training data to external service.");
        } catch (Exception e) {
            log.error("Failed to send training data to external service", e);
        }
    }

    public List<Training> getTrainingsByTrainee(Integer traineeId) {
        log.info("Fetching trainings for trainee with ID: {}", traineeId);
        List<Training> trainingList = trainingRepository.findByTraineeId(traineeId);
        log.info("Found {} trainings for trainee with ID: {}", trainingList.size(), traineeId);

        return trainingList;
    }
}
