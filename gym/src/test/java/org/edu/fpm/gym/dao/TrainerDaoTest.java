package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.storage.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerDaoTest {
    @Mock
    TrainerStorage trainerStorage;
    @InjectMocks
    TrainerDao trainerDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_TrainerIntoMap_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        when(trainerStorage.getTrainers()).thenReturn(trainers);

        Trainer Trainer = new Trainer();
        Trainer.setUserId(null);

        Trainer savedTrainer = trainerDao.save(Trainer);

        assertNotNull(savedTrainer.getUserId());
        assertTrue(trainers.containsKey(savedTrainer.getUserId()));
        verify(trainerStorage, times(2)).getTrainers();
    }

    @Test
    void update_TrainerIfExists_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        Trainer Trainer = new Trainer();
        Trainer.setUserId(1L);
        trainers.put(1L, Trainer);
        when(trainerStorage.getTrainers()).thenReturn(trainers);

        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setUserId(1L);
        updatedTrainer.setFirstName("Updated Name");

        Trainer result = trainerDao.update(updatedTrainer);

        assertEquals("Updated Name", result.getFirstName());
        assertEquals(updatedTrainer, trainers.get(1L));
        verify(trainerStorage).getTrainers();
    }

    @Test
    void update_ShouldThrowExceptionIfTrainerDoesNotExists_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        when(trainerStorage.getTrainers()).thenReturn(trainers); // порожня мапа

        Trainer nonExistentTrainer = new Trainer();
        nonExistentTrainer.setUserId(1L);
        assertThrows(IllegalArgumentException.class, () -> trainerDao.update(nonExistentTrainer));
        verify(trainerStorage).getTrainers();
    }

    @Test
    void findById_ShouldReturnTrainerIfExists_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        Trainer Trainer = new Trainer();
        Trainer.setUserId(1L);
        trainers.put(1L, Trainer);

        when(trainerStorage.getTrainers()).thenReturn(trainers);
        Trainer result = trainerDao.findById(1L);

        assertEquals(Trainer, result);
        verify(trainerStorage).getTrainers();
    }

    @Test
    void findById_ShouldReturnNullIfTrainerDoesNotExist_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        when(trainerStorage.getTrainers()).thenReturn(trainers);

        Trainer emptyTrainer = trainerDao.findById(1L);

        assertNull(emptyTrainer);
        verify(trainerStorage).getTrainers();
    }

    @Test
    void findAll_ShouldReturnAllTrainers_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        Trainer Trainer1 = new Trainer();
        Trainer1.setUserId(1L);
        Trainer1.setFirstName("John");

        Trainer Trainer2 = new Trainer();
        Trainer2.setUserId(2L);
        Trainer2.setFirstName("Doe");

        trainers.put(1L, Trainer1);
        trainers.put(2L, Trainer2);

        when(trainerStorage.getTrainers()).thenReturn(trainers);

        String result = trainerDao.findAll();

        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
        verify(trainerStorage).getTrainers();
    }

    @Test
    void generateNewId_ShouldReturnUniqId_Test() {
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(1L, new Trainer());
        trainers.put(2L, new Trainer());
        when(trainerStorage.getTrainers()).thenReturn(trainers);

        Long newId = trainerDao.generateNewId();

        assertEquals(Long.valueOf(3L), newId);
        verify(trainerStorage).getTrainers();
    }

    @Test
    void generateNewId_shouldReturnOneIfNoTrainersExist_Test() {
        when(trainerStorage.getTrainers()).thenReturn(new HashMap<>());

        Long newId = trainerDao.generateNewId();

        assertEquals(Long.valueOf(1L), newId);
        verify(trainerStorage).getTrainers();
    }
}
