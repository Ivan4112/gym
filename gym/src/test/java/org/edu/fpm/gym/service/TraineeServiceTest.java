package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.trainee.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.metrics.TraineeRepositoryMetrics;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    TraineeRepositoryMetrics traineeRepositoryMetrics;
    @Mock
    TrainerService trainerService;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    private final String username = "testuser";
    private final User user = TestDataFactory.createUser("John.Doe");
    TrainingType trainingType = TestDataFactory.createTrainingType();


    @BeforeEach
    public void setUp() {
        trainee = TestDataFactory.createTrainee(user);

        when(traineeRepositoryMetrics.measureQueryExecutionTime(any())).thenAnswer(invocation ->
                ((Supplier<Trainee>) invocation.getArgument(0)).get());

        when(traineeRepositoryMetrics.measureDataLoadTime(any())).thenAnswer(invocation ->
                ((Supplier<Trainee>) invocation.getArgument(0)).get());

        when(traineeRepositoryMetrics.measureDbConnectionTime(any())).thenAnswer(invocation ->
                ((Supplier<Trainee>) invocation.getArgument(0)).get());
    }

    @Test
    void getTraineeByUsername_Test() {
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        var result = traineeService.getTraineeByUsername(username);
        assertNotNull(result);
        assertEquals(trainee, result);
        verify(traineeRepository, times(1)).findTraineeByUser_Username(username);
    }

    @Test
    void deleteTraineeByUsername_Test() {
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        traineeService.deleteTraineeByUsername(username);
        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    void switchTraineeActivation_Test() {
        boolean isActive = true;

        traineeService.switchTraineeActivation(username, isActive);
        verify(traineeRepository, times(1)).switchActivation(username, isActive);
    }

    @Test
    void getTraineeTrainings_Test() {
        var trainingRequest = TestDataFactory.createTrainingRequestDTO();
        List<Training> trainings = List.of(
                TestDataFactory.createTraining(TestDataFactory.createTrainer(TestDataFactory.createUser("John.Doe"), trainingType))
        );

        when(traineeRepository.findTrainingsByTraineeAndDateRange(
                trainingRequest.username(),
                trainingRequest.periodFrom(),
                trainingRequest.periodTo(),
                trainingRequest.trainerName(),
                trainingRequest.trainingType()))
                .thenReturn(trainings);

        List<TrainingDTO> result = traineeService.getTraineeTrainings(trainingRequest);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardio Workout", result.getFirst().trainingName());
        assertEquals(LocalDate.now(), result.getFirst().trainingDate());
        assertEquals("John", result.getFirst().userName());
    }

    @Test
    void updateTraineeTrainers_Test() {
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");

        var trainer1 = TestDataFactory.createTrainer(user, trainingType);
        var trainer2 = TestDataFactory.createTrainer(user, trainingType);

        when(trainerService.getTrainerByUsername("trainer1")).thenReturn(trainer1);
        when(trainerService.getTrainerByUsername("trainer2")).thenReturn(trainer2);
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        List<TrainerDTO> result = traineeService.updateTraineeTrainers(username, trainerUsernames);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void getTraineeProfile_TraineeExists_Test() {
        trainee.setTrainers(new HashSet<>());

        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        var result = traineeService.getTraineeProfile(username);
        assertNotNull(result);
        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("123 Main St", result.address());
    }

    @Test
    void getTraineeProfile_TraineeNotAuthorized_Test() {
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            traineeService.getTraineeProfile(username);
        });

        assertEquals("401 UNAUTHORIZED", exception.getMessage());
    }

    @Test
    void updateTraineeProfile_TraineeExists_Test() {
        var updateProfile = new TraineeUpdateProfileDTO("testuser", "UpdatedFirstName", "UpdatedLastName",
                         LocalDate.of(1995, 5, 5), "Updated Address", true);

        trainee.setTrainers(new HashSet<>());

        when(traineeRepository.findTraineeByUser_Username("testuser")).thenReturn(trainee);

        var result = traineeService.updateTraineeProfile(updateProfile);

        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.firstName());
        assertEquals("UpdatedLastName", result.lastName());
        assertEquals("Updated Address", result.address());
        verify(traineeRepository, times(1)).updateTraineeByUserUsername(eq("testuser"), any(Trainee.class));
    }
}
