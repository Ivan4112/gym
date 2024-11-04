package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeDTO;
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
import org.edu.fpm.gym.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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

    private final UserService userService;

    private final AuthService authService;

    @Autowired
    public TraineeService(TraineeRepository traineeRepository,
                          TraineeRepositoryMetrics traineeRepositoryMetrics,
                          TrainerService trainerService, UserService userService, AuthService authService) {
        this.traineeRepository = traineeRepository;
        this.traineeRepositoryMetrics = traineeRepositoryMetrics;
        this.trainerService = trainerService;
        this.userService = userService;
        this.authService = authService;
    }

    public Trainee createTrainee(@RequestBody TraineeDTO traineeDto) {
        var user = UserUtils.getUserInstance(
                traineeDto.user().firstName(),
                traineeDto.user().lastName(),
                userService.generateUsername(traineeDto.user().firstName(), traineeDto.user().lastName()),
                userService.generatePassword(), traineeDto.user().isActive());

        var trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(traineeDto.address());
        trainee.setDateOfBirth(traineeDto.dateOfBirth());
        user.setTrainees(trainee);
        traineeRepository.save(trainee);
        userService.createUser(user);

        log.info("Created trainee with username: {}", traineeDto.user().username());

        return traineeRepositoryMetrics.measureDbConnectionTime(() ->
                traineeRepositoryMetrics.measureQueryExecutionTime(() ->
                        traineeRepository.save(trainee)
                )
        );
    }

    public Trainee getTraineeByUsername(String username, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Transaction started for fetching trainee with username: {}", username);
            return traineeRepositoryMetrics.measureQueryExecutionTime(() ->
                    traineeRepositoryMetrics.measureDataLoadTime(() ->
                            traineeRepository.findTraineeByUser_Username(username))
            );
        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
    }

    public void deleteTraineeByUsername(String username, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Transaction started for deleting trainee with username: {}", username);
            var trainee = traineeRepository.findTraineeByUser_Username(username);

            traineeRepositoryMetrics.measureQueryExecutionTime(() -> {
                        traineeRepository.delete(trainee);
                        return null;
                    }
            );

        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
    }

    public void switchTraineeActivation(String username, boolean isActive, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Transaction started for switching activation status of trainee with username: {}", username);
            traineeRepositoryMetrics.measureQueryExecutionTime(() -> {
                traineeRepository.switchActivation(username, isActive);
                return null;
            });
        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
    }


    public List<TrainingDTO> getTraineeTrainings(TrainingRequestDTO trainingRequestDTO) {
        if (authService.isAuthenticateUser(trainingRequestDTO.username(), trainingRequestDTO.password())) {
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
        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
    }

    public List<TrainerDTO> updateTraineeTrainers(String username, List<String> trainerUsernames, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Transaction started for updating trainers for trainee with username: {}", username);

            var trainee = getTraineeByUsername(username, password);

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
        } else { throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);}
    }

    public TraineeProfileDTO getTraineeProfile(String username, String password) {
        var trainee = traineeRepositoryMetrics.measureQueryExecutionTime(
                () -> traineeRepositoryMetrics.measureDataLoadTime(() -> getTraineeByUsername(username, password)));
        if (trainee == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return getTraineeProfileDTO(trainee);
    }


    public TraineeProfileDTO updateTraineeProfile(TraineeUpdateProfileDTO updateProfile, String password) {
        log.info("Transaction started for updating profile of trainee with username: {}", updateProfile.username());

        var trainee = getTraineeByUsername(updateProfile.username(), password);

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
