package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.*;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

    @InjectMocks
    private TrainingService trainingService;

    Trainee trainee;
    Trainer trainer;
    TrainingType trainingType;

    @BeforeEach
    void setUp() {
        String username = "testuser";
        User user = new User();
        user.setFirstName("traineeUser");
        user.setLastName("Doe");
        user.setUsername(username);
        user.setIsActive(true);
        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Street");

        user.setFirstName("trainerUser");
        trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(user);

        trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");
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

        boolean result = trainingService.addTraining(traineeUsername, trainerUsername, trainingName, trainingDate, trainingDuration);

        assertTrue(result);
        verify(trainingRepository, times(1)).save(any(Training.class)); // Перевірка, що метод save був викликаний
    }

    @Test
    void getTrainingsByTrainee_Test() {
        Long traineeId = 1L;
        Training training = new Training();
        training.setId(1L);
        List<Training> expectedTrainings = Collections.singletonList(training);

        when(trainingRepository.findByTraineeId(traineeId)).thenReturn(expectedTrainings);
        List<Training> result = trainingService.getTrainingsByTrainee(traineeId);

        verify(trainingRepository).findByTraineeId(traineeId);
        assertEquals(expectedTrainings, result);
    }

    @Test
    void getTrainingsByTrainer_Test() {
        Long trainerId = 1L;
        Training training = new Training();
        training.setId(1L);
        List<Training> expectedTrainings = Collections.singletonList(training);

        when(trainingRepository.findByTrainerId(trainerId)).thenReturn(expectedTrainings);
        List<Training> result = trainingService.getTrainingsByTrainer(trainerId);

        verify(trainingRepository).findByTrainerId(trainerId);
        assertEquals(expectedTrainings, result);
    }
}
