package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.*;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    private TrainingFeignClient feignClient;
    @InjectMocks
    private TrainingService trainingService;

    Trainee trainee;
    Trainer trainer;
    Training training;
    TrainingType trainingType;
    private final Integer ID = 1;

    @BeforeEach
    void setUp() {
        String username = "testuser";
        var user = TestDataFactory.createUser(username);
        trainingType = TestDataFactory.createTrainingType();
        trainee = TestDataFactory.createTrainee(user);
        trainer = TestDataFactory.createTrainer(user, trainingType);
        training = new Training();
    }

    @Test
    void addTraining_Test() {
        String traineeUsername = "traineeUser";
        String trainerUsername = "trainerUser";
        String trainingName = "Yoga";
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(traineeRepository.findTraineeByUser_Username(traineeUsername)).thenReturn(trainee);
        when(trainerRepository.findTrainerByUser_Username(trainerUsername)).thenReturn(trainer);
        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingName)).thenReturn(trainingType);

        var addTrainingDTO = new AddTrainingDTO(traineeUsername, trainerUsername,
                trainingName, trainingDate, trainingDuration);

        var response = trainingService.addTraining(addTrainingDTO);
        assertEquals("Training added successfully", response);
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void getTrainingsByTrainee_Test() {
        training.setId(1);
        List<Training> expectedTrainings = Collections.singletonList(training);

        when(trainingRepository.findByTraineeId(ID)).thenReturn(expectedTrainings);
        List<Training> result = trainingService.getTrainingsByTrainee(ID);

        verify(trainingRepository).findByTraineeId(ID);
        assertEquals(expectedTrainings, result);
    }

    @Test
    public void deleteTraining_Success_Test() {
        Integer trainingId = 1;
        LocalDate trainingDate = LocalDate.now().plusDays(2);
        Training training = new Training(trainingId, 10, "Cardio",
                TestDataFactory.createTrainee(TestDataFactory.createUser("Username")),
                TestDataFactory.createTrainer(TestDataFactory.createUser("Username"), TestDataFactory.createTrainingType()),
                TestDataFactory.createTrainingType(), trainingDate);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        trainingService.deleteTraining(trainingId);

        verify(trainingRepository, times(1)).deleteById(trainingId);
        verify(feignClient, times(1)).updateWorkload(any(ExternalTrainingServiceDTO.class));
    }

    @Test
    public void deleteTraining_Failed_DueToPastDate_Test() {
        Integer trainingId = 1;
        LocalDate trainingDate = LocalDate.now().minusDays(1);
        Training training = new Training(trainingId, 10, "Cardio",
                TestDataFactory.createTrainee(TestDataFactory.createUser("Username")),
                TestDataFactory.createTrainer(TestDataFactory.createUser("Username"), TestDataFactory.createTrainingType()),
                TestDataFactory.createTrainingType(), trainingDate);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> trainingService.deleteTraining(trainingId));
        assertEquals("Cannot delete training with ID 1. It has already been completed on " + trainingDate, thrown.getMessage().trim());
    }

    @Test
    public void deleteTraining_Failed_DueToLessThan24Hours_Test() {
        Integer trainingId = 1;
        LocalDate trainingDate = LocalDate.now();
        Training training = new Training(trainingId, 10, "Cardio",
                TestDataFactory.createTrainee(TestDataFactory.createUser("Username")),
                TestDataFactory.createTrainer(TestDataFactory.createUser("Username"), TestDataFactory.createTrainingType()),
                TestDataFactory.createTrainingType(), trainingDate);

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> trainingService.deleteTraining(trainingId));
        assertEquals("Cannot delete training with ID 1. It can only be deleted at least 24 hours in advance.", thrown.getMessage());
    }

    @Test
    public void deleteTraining_TrainingNotFound_Test() {
        Integer trainingId = 1;

        when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

        trainingService.deleteTraining(trainingId);

        verify(trainingRepository, times(1)).findById(trainingId);
        verify(trainingRepository, never()).deleteById(trainingId);
    }


    @Test
    public void getTrainingsByTrainer_Success_Test() {
        Integer trainerId = 1;
        Training training = new Training(trainerId, 10, "Cardio",
                TestDataFactory.createTrainee(TestDataFactory.createUser("Username")),
                TestDataFactory.createTrainer(TestDataFactory.createUser("Username"), TestDataFactory.createTrainingType()),
                TestDataFactory.createTrainingType(), LocalDate.now());

        List<Training> trainingList = List.of(training);

        when(trainingRepository.findByTrainerId(trainerId)).thenReturn(trainingList);
        when(feignClient.initializeTrainerWorkload(anyList())).thenReturn(ResponseEntity.ok("Success"));

        ResponseEntity<List<ExternalTrainingServiceDTO>> response = trainingService.getTrainingsByTrainer(trainerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Username", response.getBody().getFirst().trainerUsername());
        verify(feignClient, times(1)).initializeTrainerWorkload(anyList());
    }

    @Test
    public void getTrainingsByTrainer_NoContent_Test() {
        Integer trainerId = 1;
        when(trainingRepository.findByTrainerId(trainerId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ExternalTrainingServiceDTO>> response = trainingService.getTrainingsByTrainer(trainerId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        assertNull(response.getBody(), "Response body should be null when no trainings are found");

        verify(feignClient, never()).initializeTrainerWorkload(anyList());
    }
}
