package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.entity.*;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    AuthService authService;

    @InjectMocks
    private TrainingService trainingService;

    Trainee trainee;
    Trainer trainer;
    Training training;
    TrainingType trainingType;
    private final Long ID = 1L;

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
        String password = "password";
        String trainingName = "Yoga";
        LocalDate trainingDate = LocalDate.now();
        int trainingDuration = 60;

        when(authService.isAuthenticateUser(traineeUsername, password)).thenReturn(true);
        when(traineeRepository.findTraineeByUser_Username(traineeUsername)).thenReturn(trainee);
        when(trainerRepository.findTrainerByUser_Username(trainerUsername)).thenReturn(trainer);
        when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainingName)).thenReturn(trainingType);

        var addTrainingDTO = new AddTrainingDTO(traineeUsername, trainerUsername,
                trainingName, trainingDate, trainingDuration);

        var response = trainingService.addTraining(addTrainingDTO, password);
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
    void getTrainingsByTrainer_Test() {
        training.setId(1);
        List<Training> expectedTrainings = Collections.singletonList(training);

        when(trainingRepository.findByTrainerId(ID)).thenReturn(expectedTrainings);
        List<Training> result = trainingService.getTrainingsByTrainer(ID);

        verify(trainingRepository).findByTrainerId(ID);
        assertEquals(expectedTrainings, result);
    }
}
