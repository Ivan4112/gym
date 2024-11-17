package org.edu.fpm.gym.service;

import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserService userService;

    @Mock
    AuthService authService;

    @InjectMocks
    private TrainerService trainerService;



    private Trainer trainer;
    private TrainingType trainingType;
    private TrainerUpdateProfileDTO updateProfile;

    private final String username = "john.doe";
    private final String password = "password";

    @BeforeEach
    public void setUp() {
        trainingType = TestDataFactory.createTrainingType();

        updateProfile = new TrainerUpdateProfileDTO("john.doe", "UpdatedJohn",
                        "UpdatedDoe", trainingType, true);

        trainer = TestDataFactory.createTrainer(TestDataFactory.createUser("john.doe"), trainingType);
    }

    @Test
    void createTrainer_Test() {
        String firstName = "John";
        String lastName = "Doe";

        var trainerDTO = new TrainerDTO(
                new UserDTO(firstName, lastName, username, true), trainingType);

        when(userService.generateUsername(firstName, lastName)).thenReturn(username);
        when(userService.generatePassword()).thenReturn(password);

        when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainerDTO);

        verify(trainerRepository, times(2)).save(any(Trainer.class));

        assertEquals(username, createdTrainer.getUser().getUsername());
        assertEquals(password, createdTrainer.getUser().getPassword());
        assertEquals(firstName, createdTrainer.getUser().getFirstName());
        assertEquals(lastName, createdTrainer.getUser().getLastName());
        assertEquals(trainingType, createdTrainer.getSpecialization());
    }

    @Test
    void getTrainerByUsername_Test() {
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);
        Trainer foundTrainer = trainerService.getTrainerByUsername(username);
        verify(trainerRepository).findTrainerByUser_Username(username);
        assertEquals(trainer, foundTrainer);
    }

    @Test
    void switchTrainerActivation_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);

        boolean isActive = true;

        trainerService.switchTrainerActivation(username, isActive, password);

        verify(trainerRepository, times(1)).switchActivation(username, isActive);
    }

    @Test
    void deleteTrainer_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        trainerService.deleteTrainer(username, password);
        verify(trainerRepository).deleteTrainerByUser_Username(username);
    }

    @Test
    @SneakyThrows
    void getAvailableTrainersForTrainee_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        when(trainerRepository.findAvailableTrainersForTrainee(username)).thenReturn(Collections.singletonList(trainer));

        List<Trainer> availableTrainers = trainerService.getAvailableTrainersForTrainee(username, password);

        verify(trainerRepository).findAvailableTrainersForTrainee(username);
        assertEquals(1, availableTrainers.size());
        assertEquals(trainer, availableTrainers.getFirst());
    }
    @Test
    @SneakyThrows
    void getTrainingsForTrainer_Test() {
        var training = TestDataFactory.createTraining(trainer);
        var trainingRequest = TestDataFactory.createTrainingRequestDTO();
        var trainee = new Trainee();
        trainee.setUser(new User("Trainee", "John", "Doe", true));
        training.setTrainee(trainee);

        when(authService.isAuthenticateUser(trainingRequest.username(), trainingRequest.password())).thenReturn(true);
        when(trainerRepository.getTrainingsForTrainer(
                trainingRequest.username(),
                trainingRequest.periodFrom(),
                trainingRequest.periodTo(),
                trainingRequest.trainerName()))
                .thenReturn(List.of(training));

        List<TrainingDTO> trainings = trainerService.getTrainingsForTrainer(trainingRequest);

        assertNotNull(trainings);
        assertEquals(1, trainings.size());
        assertEquals("Cardio Workout", trainings.getFirst().trainingName());
        assertEquals("John", trainings.getFirst().userName());
    }

    @Test
    @SneakyThrows
    void getTrainerProfile_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);

        var profile = trainerService.getTrainerProfile(username, password);

        assertNotNull(profile);
        assertEquals("John", profile.firstName());
        assertEquals("Doe", profile.lastName());
        assertEquals("Cardio", profile.trainingType().getTrainingTypeName());
    }

    @Test
    @SneakyThrows
    void updateTrainerProfile_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);
        when(trainerRepository.updateTrainerByUserUsername(anyString(), any(Trainer.class))).thenReturn(trainer);

        var updatedProfile = trainerService.updateTrainerProfile(updateProfile, password);

        assertNotNull(updatedProfile);
        assertEquals("UpdatedJohn", updatedProfile.firstName());
        assertEquals("UpdatedDoe", updatedProfile.lastName());
        assertEquals("Cardio", updatedProfile.trainingType().getTrainingTypeName());
    }

    @Test
    @SneakyThrows
    void getTrainerProfile_TrainerNotFound_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> trainerService.getTrainerProfile(username, password));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Trainer not found\"", exception.getMessage());
    }
}
