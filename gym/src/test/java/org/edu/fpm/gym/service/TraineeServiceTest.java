package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TraineeDao;
import org.edu.fpm.gym.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceTest {
    @Mock
    TraineeDao traineeDao;

    @InjectMocks
    TraineeService traineeService;

    Trainee trainee;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainee = new Trainee(1L, "Chris", "Evans",
                "chris.evans", "password123", true, LocalDate.now(),"321 Pine St");

    }

    @Test
    void createTrainee_Test() {
        when(traineeDao.save(trainee)).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertEquals(trainee, createdTrainee);
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void updateTrainee_Test() {
        when(traineeDao.update(trainee)).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertEquals(trainee, updatedTrainee);
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void deleteTrainee_Test() {
        Long traineeId = 1L;

        doNothing().when(traineeDao).delete(traineeId);

        traineeService.deleteTrainee(traineeId);

        verify(traineeDao, times(1)).delete(traineeId);
    }

    @Test
    void getTraineeById_Test() {
        Long traineeId = 1L;
        when(traineeDao.findById(traineeId)).thenReturn(trainee);

        Trainee foundTrainee = traineeService.getTraineeById(traineeId);

        assertEquals(trainee, foundTrainee);
        verify(traineeDao, times(1)).findById(traineeId);
    }

    @Test
    void getAllTrainees_Test() {
        traineeDao.save(trainee);
        String expectedOutput = trainee.toString();

        when(traineeDao.findAll()).thenReturn(expectedOutput);

        String foundTrainees = traineeService.getAllTrainees();
        System.out.println(foundTrainees);

        assertEquals(expectedOutput, foundTrainees);
        verify(traineeDao, times(1)).findAll();
    }
}
