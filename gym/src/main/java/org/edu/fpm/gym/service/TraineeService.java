package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeProfileDTO;
import org.edu.fpm.gym.dto.trainee.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.metrics.TraineeRepositoryMetrics;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TraineeService {

    private final TraineeRepository traineeRepository;

    private final TraineeRepositoryMetrics traineeRepositoryMetrics;

    private final TrainerService trainerService;


    @Autowired
    public TraineeService(TraineeRepository traineeRepository,
                          TraineeRepositoryMetrics traineeRepositoryMetrics,
                          TrainerService trainerService) {
        this.traineeRepository = traineeRepository;
        this.traineeRepositoryMetrics = traineeRepositoryMetrics;
        this.trainerService = trainerService;
    }

    public Trainee getTraineeByUsername(String username) {
        log.info("Transaction started for fetching trainee with username: {}", username);
        return traineeRepositoryMetrics.measureQueryExecutionTime(() ->
                traineeRepositoryMetrics.measureDataLoadTime(() ->
                        traineeRepository.findTraineeByUser_Username(username))
        );
    }

    public void deleteTraineeByUsername(String username) {
        log.info("Transaction started for deleting trainee with username: {}", username);
        var trainee = traineeRepository.findTraineeByUser_Username(username);

        traineeRepositoryMetrics.measureQueryExecutionTime(() -> {
                    traineeRepository.delete(trainee);
                    return null;
                }
        );
    }

    public void switchTraineeActivation(String username, boolean isActive) {
        log.info("Transaction started for switching activation status of trainee with username: {}", username);
        traineeRepositoryMetrics.measureQueryExecutionTime(() -> {
            traineeRepository.switchActivation(username, isActive);
            return null;
        });
    }


    public List<TrainingDTO> getTraineeTrainings(TrainingRequestDTO trainingRequestDTO) {
        log.info("Transaction started for fetching trainings for trainee with username: {}", trainingRequestDTO.username());

        List<Training> trainings = traineeRepositoryMetrics.measureDataLoadTime(() ->
                traineeRepository.findTrainingsByTraineeAndDateRange(
                        trainingRequestDTO.username(),
                        trainingRequestDTO.periodFrom(),
                        trainingRequestDTO.periodTo(),
                        trainingRequestDTO.trainerName(),
                        trainingRequestDTO.trainingType()
                )
        );

        return trainings.stream().map(
                training -> new TrainingDTO(
                        training.getTrainingName(),
                        training.getTrainingDate(),
                        training.getTrainingType(),
                        training.getTrainingDuration(),
                        training.getTrainer().getUser().getFirstName())
        ).toList();
    }

    public List<TrainerDTO> updateTraineeTrainers(String username, List<String> trainerUsernames) {
        log.info("Transaction started for updating trainers for trainee with username: {}", username);

        var trainee = getTraineeByUsername(username);

        Set<Trainer> trainers = trainerUsernames.stream()
                .map(trainerService::getTrainerByUsername)
                .collect(Collectors.toSet());

        trainee.setTrainers(trainers);
        traineeRepository.save(trainee);

        return trainers.stream().map(
                trainer -> new TrainerDTO(
                        new UserDTO(
                                trainer.getUser().getUsername(),
                                trainer.getUser().getFirstName(),
                                trainer.getUser().getLastName(),
                                trainer.getUser().getIsActive()),
                        trainer.getSpecialization())
        ).toList();
    }

    public TraineeProfileDTO getTraineeProfile(String username) {
        var trainee = traineeRepositoryMetrics.measureQueryExecutionTime(
                () -> traineeRepositoryMetrics.measureDataLoadTime(() -> getTraineeByUsername(username)));
        if (trainee == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return getTraineeProfileDTO(trainee);
    }


    public TraineeProfileDTO updateTraineeProfile(TraineeUpdateProfileDTO updateProfile) {
        log.info("Transaction started for updating profile of trainee with username: {}", updateProfile.username());

        var trainee = getTraineeByUsername(updateProfile.username());

        if (trainee == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {
            var user = trainee.getUser();
            user.setFirstName(updateProfile.firstName());
            user.setLastName(updateProfile.lastName());
            user.setUsername(updateProfile.username());
            user.setIsActive(updateProfile.isActive());

            trainee.setDateOfBirth(updateProfile.dateOfBirth());
            trainee.setAddress(updateProfile.address());
            trainee.setUser(user);
            traineeRepository.updateTraineeByUserUsername(trainee.getUser().getUsername(), trainee);
        }
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
