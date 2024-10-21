//package org.edu.fpm.gym.dao;
//
//import org.edu.fpm.gym.storage.TraineeStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeDaoTest {
//    @Mock
//    TraineeStorage traineeStorage;
//    @InjectMocks
//    TraineeDao traineeDao;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void save_TraineeIntoMap_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        Trainee trainee = new Trainee();
//        trainee.setUserId(null);
//
//        Trainee savedTrainee = traineeDao.save(trainee);
//
//        assertNotNull(savedTrainee.getUserId());
//        assertTrue(trainees.containsKey(savedTrainee.getUserId()));
//        verify(traineeStorage, times(2)).getTrainees();
//    }
//
//    @Test
//    void update_TraineeIfExists_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//        trainees.put(1L, trainee);
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        Trainee updatedTrainee = new Trainee();
//        updatedTrainee.setUserId(1L);
//        updatedTrainee.setFirstName("Updated Name");
//
//        Trainee result = traineeDao.update(updatedTrainee);
//
//        assertEquals("Updated Name", result.getFirstName());
//        assertEquals(updatedTrainee, trainees.get(1L));
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void update_ShouldThrowExceptionIfTraineeDoesNotExists_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        when(traineeStorage.getTrainees()).thenReturn(trainees); // порожня мапа
//
//        Trainee nonExistentTrainee = new Trainee();
//        nonExistentTrainee.setUserId(1L);
//        assertThrows(IllegalArgumentException.class, () -> traineeDao.update(nonExistentTrainee));
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void delete_TraineeIfExists_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//        trainees.put(1L, trainee);
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        traineeDao.delete(1L);
//
//        assertFalse(trainees.containsKey(1L));
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void delete_ShouldThrowExceptionIfTraineeDoesNotExists_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee nonExistentTrainee = new Trainee();
//
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        assertThrows(IllegalArgumentException.class, () -> traineeDao.delete(nonExistentTrainee.getUserId()));
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void findById_ShouldReturnTraineeIfExists_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee trainee = new Trainee();
//        trainee.setUserId(1L);
//        trainees.put(1L, trainee);
//
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//        Trainee result = traineeDao.findById(1L);
//
//        assertEquals(trainee, result);
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void findById_ShouldReturnNullIfTraineeDoesNotExist_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        Trainee emptyTrainee = traineeDao.findById(1L);
//
//        assertNull(emptyTrainee);
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void findAll_ShouldReturnAllTrainees_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee trainee1 = new Trainee();
//        trainee1.setUserId(1L);
//        trainee1.setFirstName("John");
//
//        Trainee trainee2 = new Trainee();
//        trainee2.setUserId(2L);
//        trainee2.setFirstName("Doe");
//
//        trainees.put(1L, trainee1);
//        trainees.put(2L, trainee2);
//
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        String result = traineeDao.findAll();
//
//        assertTrue(result.contains("John"));
//        assertTrue(result.contains("Doe"));
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void generateNewId_ShouldReturnUniqId_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        trainees.put(1L, new Trainee());
//        trainees.put(2L, new Trainee());
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//
//        Long newId = traineeDao.generateNewId();
//
//        assertEquals(Long.valueOf(3L), newId);
//        verify(traineeStorage).getTrainees();
//    }
//
//    @Test
//    void generateNewId_shouldReturnOneIfNoTraineesExist_Test() {
//        when(traineeStorage.getTrainees()).thenReturn(new HashMap<>());
//
//        Long newId = traineeDao.generateNewId();
//
//        assertEquals(Long.valueOf(1L), newId);
//        verify(traineeStorage).getTrainees();
//    }
//}
