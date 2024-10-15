package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TrainingDao;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {
    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingService trainingService;

    Training training;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        training = new Training(1L, 2L, "Training 1", TrainingType.YOGA, LocalDate.now(), 60);

    }

    @Test
    public void createTraining_Test(){
        when(trainingDao.save(training)).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertNotNull(createdTraining);
        assertEquals("Training 1", createdTraining.getTrainingName());
        verify(trainingDao, times(1)).save(training);
    }

    @Test
    public void getTrainingById_Test(){
        Long trainingId = 1L;

        when(trainingDao.findById(trainingId)).thenReturn(training);

        Training foundTraining = trainingService.getTrainingById(trainingId);

        assertNotNull(foundTraining);
        assertEquals(1L, foundTraining.getTraineeId());
        verify(trainingDao, times(1)).findById(trainingId);
    }

    @Test
    public void getAllTrainings_Test(){
        trainingDao.save(training);
        String expectedOutput = training.toString();

        when(trainingDao.findAll()).thenReturn(expectedOutput);

        String foundTrainings = trainingService.getAllTrainings();
        System.out.println(foundTrainings);

        assertEquals(expectedOutput, foundTrainings);
        verify(trainingDao, times(1)).findAll();
    }
}
