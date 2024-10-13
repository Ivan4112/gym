package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TrainerDao;
import org.edu.fpm.gym.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    @Mock
    TrainerDao trainerDao;

    @InjectMocks
    TrainerService trainerService;
    Trainer trainer;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        trainer = new Trainer(1L, "Chris", "Evans",
                "chris.evans", "password123", true, "Cardio");

    }

    @Test
    void createTrainerTest() {
        when(trainerDao.save(trainer)).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertEquals(trainer, createdTrainer);
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void updateTrainerTest() {
        when(trainerDao.update(trainer)).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertEquals(trainer, updatedTrainer);
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void getTrainerByIdTest() {
        Long id = 1L;
        when(trainerDao.findById(id)).thenReturn(trainer);

        Trainer foundTrainer = trainerService.getTrainerById(id);

        assertEquals(trainer, foundTrainer);
        verify(trainerDao, times(1)).findById(id);
    }

    @Test
    void getAllTrainersTest() {
        trainerDao.save(trainer);
        String expectedOutput = trainer.toString();

        when(trainerDao.findAll()).thenReturn(expectedOutput);

        String foundTrainers = trainerService.getAllTrainers();
        System.out.println(foundTrainers);

        assertEquals(expectedOutput, foundTrainers);
        verify(trainerDao, times(1)).findAll();
    }
}
