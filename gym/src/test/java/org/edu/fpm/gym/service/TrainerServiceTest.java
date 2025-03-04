package org.edu.fpm.gym.service;

import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.externalservice.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.service.messaging.listener.TrainerEventListener;
import org.edu.fpm.gym.service.messaging.producer.TrainerEventProducer;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    TrainerEventListener trainerEventListener;
    @Mock
    TrainerEventProducer trainerEventProducer;
    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private TrainingType trainingType;
    private TrainerUpdateProfileDTO updateProfile;

    private final String username = "john.doe";

    @BeforeEach
    public void setUp() {
        trainingType = TestDataFactory.createTrainingType();

        updateProfile = new TrainerUpdateProfileDTO("john.doe", "UpdatedJohn",
                        "UpdatedDoe", trainingType, true);

        trainer = TestDataFactory.createTrainer(TestDataFactory.createUser("john.doe"), trainingType);
    }

    @Test
    void getTrainerMonthlyWorkload_Success_Test() {
        TrainerWorkloadSummaryDTO mockResponse = TestDataFactory.createTrainerWorkloadSummaryDTO(username);

        CompletableFuture<TrainerWorkloadSummaryDTO> futureResponse = CompletableFuture.completedFuture(mockResponse);
        when(trainerEventListener.getFutureResponse(username)).thenReturn(futureResponse);

        TrainerWorkloadSummaryDTO result = trainerService.getTrainerMonthlyWorkload(username);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals("firstname", result.firstName());
        assertEquals("lastname", result.lastName());
        assertTrue(result.isActive());
    }

    @Test
    void getTrainerMonthlyWorkload_Fail_Test() {
        CompletableFuture<TrainerWorkloadSummaryDTO> failedFuture = new CompletableFuture<>();
        failedFuture.completeExceptionally(new RuntimeException("Timeout occurred"));

        when(trainerEventListener.getFutureResponse(username)).thenReturn(failedFuture);

        TrainerWorkloadSummaryDTO result = trainerService.getTrainerMonthlyWorkload(username);

        assertNotNull(result);
        assertEquals(username, result.username());
        assertEquals("", result.firstName());
        assertEquals("", result.lastName());
        assertFalse(result.isActive());
        assertTrue(result.years().isEmpty());
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
        boolean isActive = true;

        trainerService.switchTrainerActivation(username, isActive);
        verify(trainerRepository, times(1)).switchActivation(username, isActive);
    }

    @Test
    void deleteTrainer_Test() {
        trainerService.deleteTrainer(username);
        verify(trainerRepository).deleteTrainerByUser_Username(username);
    }

    @Test
    @SneakyThrows
    void getAvailableTrainersForTrainee_Test() {
        when(trainerRepository.findAvailableTrainersForTrainee(username)).thenReturn(Collections.singletonList(trainer));

        List<Trainer> availableTrainers = trainerService.getAvailableTrainersForTrainee(username);
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
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);
        var profile = trainerService.getTrainerProfile(username);
        assertNotNull(profile);
        assertEquals("John", profile.firstName());
        assertEquals("Doe", profile.lastName());
        assertEquals("Cardio", profile.trainingType().getTrainingTypeName());
    }

    @Test
    @SneakyThrows
    void updateTrainerProfile_Test() {
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);
        when(trainerRepository.updateTrainerByUserUsername(anyString(), any(Trainer.class))).thenReturn(trainer);

        var updatedProfile = trainerService.updateTrainerProfile(updateProfile);

        assertNotNull(updatedProfile);
        assertEquals("UpdatedJohn", updatedProfile.firstName());
        assertEquals("UpdatedDoe", updatedProfile.lastName());
        assertEquals("Cardio", updatedProfile.trainingType().getTrainingTypeName());
    }

    @Test
    @SneakyThrows
    void getTrainerProfile_TrainerNotFound_Test() {
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> trainerService.getTrainerProfile(username));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("404 NOT_FOUND \"Trainer not found\"", exception.getMessage());
    }
}
