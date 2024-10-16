package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.storage.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainingDaoTest {
    @Mock
    TrainingStorage trainingStorage;
    @InjectMocks
    TrainingDao trainingDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void save_ShouldReturnTraining_Test() {
        Map<Long, Training> trainings = new HashMap<>();
        Training training = new Training(1L, 1L, "Yoga", TrainingType.YOGA, LocalDate.now(), 60);

        when(trainingStorage.getTrainings()).thenReturn(trainings);
        Training savedTraining = trainingDao.save(training);

        assertTrue(trainings.containsValue(savedTraining));
        verify(trainingStorage).getTrainings();
        assertEquals(training, savedTraining);
    }

    @Test
    void findById_ShouldReturnTrainingIfExists_Test() {
        Training training = new Training(1L, 1L, "Yoga", TrainingType.YOGA, LocalDate.now(), 60);
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(1L, training);

        when(trainingStorage.getTrainings()).thenReturn(trainings);
        Training foundTraining = trainingDao.findById(1L);

        assertNotNull(foundTraining);
        assertEquals(training, foundTraining);
        verify(trainingStorage).getTrainings();
    }

    @Test
    void findById_ShouldReturnNullIfTrainingDoesNotExist_Test() {
        Map<Long, Training> trainings = new HashMap<>();
        when(trainingStorage.getTrainings()).thenReturn(trainings);

        Training foundTraining = trainingDao.findById(1L);

        assertNull(foundTraining);
        verify(trainingStorage).getTrainings();
    }

    @Test
    void findAll_ShouldReturnStringWithAllTrainings_Test() {
        Map<Long, Training> trainings = new HashMap<>();
        Training training1 = new Training(1L, 1L, "Yoga", TrainingType.YOGA, LocalDate.now(), 60);
        Training training2 = new Training(2L, 2L, "Cardio", TrainingType.CARDIO, LocalDate.now(), 60);
        trainings.put(1L, training1);
        trainings.put(2L, training2);

        when(trainingStorage.getTrainings()).thenReturn(trainings);
        String allTrainings = trainingDao.findAll();

        String expected = training1 + "\n" + training2;
        assertEquals(expected, allTrainings);
        verify(trainingStorage).getTrainings();
    }
}
