package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void createTraining_Test() {
        Training training = new Training();
        training.setId(1L);

        when(trainingRepository.save(training)).thenReturn(training);
        Training result = trainingService.createTraining(training);

        verify(trainingRepository).save(training);
        assertEquals(training, result);
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
