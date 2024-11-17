package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainer.TraineeForTrainerDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final AuthService authService;


    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserService userService, AuthService authService) {
        this.trainerRepository = trainerRepository;
        this.userService = userService;
        this.authService = authService;
    }


    public Trainer createTrainer(TrainerDTO trainerDTO) {
        Trainer createdTrainer;
        log.info("Creating trainer: {}", trainerDTO.user().firstName());
        try {

            User user = UserUtils.getUserInstance(
                    trainerDTO.user().firstName(),
                    trainerDTO.user().lastName(),
                    userService.generateUsername(trainerDTO.user().firstName(), trainerDTO.user().lastName()),
                    userService.generatePassword(), trainerDTO.user().isActive());

            var trainer = new Trainer();
            trainer.setUser(user);
            trainer.setSpecialization(trainerDTO.specialization());
            trainerRepository.save(trainer);
            userService.createUser(user);

            createdTrainer = trainerRepository.save(trainer);
        } catch (Exception e) {
            throw new RuntimeException("Error creating trainer");
        }

        log.info("Trainer created successfully: {}", createdTrainer.getUser().getUsername());
        return createdTrainer;
    }

    public Trainer getTrainerByUsername(String username) {
        log.info("Fetching trainer with username: {}", username);
        return trainerRepository.findTrainerByUser_Username(username);
    }

    public void switchTrainerActivation(String username, boolean isActive, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Switching activation for trainer: {} to {}", username, isActive);
            trainerRepository.switchActivation(username, isActive);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        log.info("Trainer activation switched: {} -> {}", username, isActive);
    }

    public void deleteTrainer(String username, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Deleting trainer with username: {}", username);
            trainerRepository.deleteTrainerByUser_Username(username);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        log.info("Trainer deleted: {}", username);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String username, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Fetching available trainers for trainee: {}", username);
            return trainerRepository.findAvailableTrainersForTrainee(username);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }


    public List<TrainingDTO> getTrainingsForTrainer(TrainingRequestDTO trainingRequestDTO) {
        if (authService.isAuthenticateUser(trainingRequestDTO.username(), trainingRequestDTO.password())) {
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
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public TrainerProfileDTO getTrainerProfile(String username, String password) {
        if (authService.isAuthenticateUser(username, password)) {
            log.info("Fetching trainer profile with username: {}", username);

            var trainer = getTrainerByUsername(username);
            if (trainer == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainer not found");
            } else {
                return getTrainerProfileDTO(trainer);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public TrainerProfileDTO updateTrainerProfile(TrainerUpdateProfileDTO updateProfile, String password) {
        if (authService.isAuthenticateUser(updateProfile.username(), password)) {
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
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
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
