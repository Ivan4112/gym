package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.dto.trainer.TraineeForTrainerDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.TrainingFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainingFeignClient feignClient;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, TrainingFeignClient feignClient) {
        this.trainerRepository = trainerRepository;
        this.feignClient = feignClient;
    }


    public TrainerWorkloadSummaryDTO getTrainerMonthlyWorkload(String username) {
        log.info("Fetching monthly workload summary for trainer with username: {}", username);

        try {
            TrainerWorkloadSummaryDTO response = feignClient.getMonthlyWorkload(username);
            log.info("Response -> {}", response);
            if (response != null) {
                log.info("Successfully fetched monthly workload summary for username: {}", username);
                return response;
            }
        } catch (RestClientException ex) {
            log.error("Error fetching monthly workload summary for username: {}. Exception: {}", username, ex.getMessage());
        }

        log.warn("Returning empty summary for username: {}", username);
        return new TrainerWorkloadSummaryDTO(username, "", "", false, new HashMap<>());

    }

    public Trainer getTrainerByUsername(String username) {
        log.info("Fetching trainer with username: {}", username);
        return trainerRepository.findTrainerByUser_Username(username);
    }

    public void switchTrainerActivation(String username, boolean isActive) {
        log.info("Switching activation for trainer: {} to {}", username, isActive);
        trainerRepository.switchActivation(username, isActive);
        log.info("Trainer activation switched: {} -> {}", username, isActive);
    }

    public void deleteTrainer(String username) {
        log.info("Deleting trainer with username: {}", username);
        trainerRepository.deleteTrainerByUser_Username(username);
        log.info("Trainer deleted: {}", username);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String username) {
        log.info("Fetching available trainers for trainee: {}", username);
        return trainerRepository.findAvailableTrainersForTrainee(username);
    }


    public List<TrainingDTO> getTrainingsForTrainer(TrainingRequestDTO trainingRequestDTO) {
        log.info("Fetching trainings for trainer: {}", trainingRequestDTO.username());

        List<Training> trainingList = trainerRepository.getTrainingsForTrainer(
                trainingRequestDTO.username(),
                trainingRequestDTO.periodFrom(),
                trainingRequestDTO.periodTo(),
                trainingRequestDTO.trainerName());

        log.info("Found {} trainings for trainer: {}", trainingList.size(), trainingRequestDTO.username());

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
        log.info("Fetching trainer profile with username: {}", username);

        var trainer = getTrainerByUsername(username);
        if (trainer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found");
        } else {
            return getTrainerProfileDTO(trainer);
        }
    }

    public TrainerProfileDTO updateTrainerProfile(TrainerUpdateProfileDTO updateProfile) {
        log.info("Updating trainer profile: {}", updateProfile.username());

        var trainer = getTrainerByUsername(updateProfile.username());

        var user = trainer.getUser();
        user.setFirstName(updateProfile.firstName());
        user.setLastName(updateProfile.lastName());
        user.setUsername(updateProfile.username());
        user.setIsActive(updateProfile.isActive());

        trainer.setSpecialization(updateProfile.trainingType());
        trainer.setUser(user);
        trainerRepository.updateTrainerByUserUsername(trainer.getUser().getUsername(), trainer);
        log.info("Trainer profile updated: {}", updateProfile.username());
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
