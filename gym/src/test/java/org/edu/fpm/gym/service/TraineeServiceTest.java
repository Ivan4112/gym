//package org.edu.fpm.gym.service;
//
//import org.edu.fpm.gym.dao.TraineeDao;
//import org.edu.fpm.gym.security.SecurityCredential;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class TraineeServiceTest {
//    @Mock
//    TraineeDao traineeDao;
//    @Mock
//    SecurityCredential securityCredential;
//    @InjectMocks
//    TraineeService traineeService;
//    Trainee trainee;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        trainee = new Trainee(1L, "Chris", "Evans",
//                "chris.evans", "password123", true, LocalDate.now(),"321 Pine St");
//
//    }
//
//    @Test
//    void createTrainee_ShouldReturnTrainee_Test() {
//        when(traineeDao.save(trainee)).thenReturn(trainee);
//
//        Trainee createdTrainee = traineeService.createTrainee(trainee);
//
//        assertEquals(trainee, createdTrainee);
//        verify(traineeDao, times(1)).save(trainee);
//    }
//
//    @Test
//    void createTrainee_ShouldReturnTraineeWithGeneratedPasswordAndUsername_Test() {
//        String firstName = "John";
//        String lastName = "Doe";
//        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);
//        String address = "123 Main St";
//        Long newId = 1L;
//        String generatedUsername = "John.Doe";
//        String generatedPassword = "randomPassword";
//
//        when(traineeDao.generateNewId()).thenReturn(newId);
//        when(securityCredential.generateUsername(firstName, lastName, newId)).thenReturn(generatedUsername);
//        when(securityCredential.generatePassword()).thenReturn(generatedPassword);
//
//        Trainee expectedTrainee = new Trainee(newId, firstName, lastName, generatedUsername, generatedPassword, true, dateOfBirth, address);
//        when(traineeDao.save(any(Trainee.class))).thenReturn(expectedTrainee);
//
//        Trainee result = traineeService.createTrainee(firstName, lastName, dateOfBirth, address);
//
//        verify(traineeDao).generateNewId();
//        verify(securityCredential).generateUsername(firstName, lastName, newId);
//        verify(securityCredential).generatePassword();
//        verify(traineeDao).save(any(Trainee.class));
//        assertEquals(expectedTrainee, result);
//    }
//    @Test
//    void updateTrainee_ShouldReturnTrainee_Test() {
//        when(traineeDao.update(trainee)).thenReturn(trainee);
//
//        Trainee updatedTrainee = traineeService.updateTrainee(trainee);
//
//        assertEquals(trainee, updatedTrainee);
//        verify(traineeDao, times(1)).update(trainee);
//    }
//
//    @Test
//    void deleteTrainee_ShouldDeleteIfExists_Test() {
//        Long traineeId = 1L;
//
//        doNothing().when(traineeDao).delete(traineeId);
//
//        traineeService.deleteTrainee(traineeId);
//
//        verify(traineeDao, times(1)).delete(traineeId);
//    }
//
//    @Test
//    void getTraineeById_ShouldReturnTrainee_Test() {
//        Long traineeId = 1L;
//        when(traineeDao.findById(traineeId)).thenReturn(trainee);
//
//        Trainee foundTrainee = traineeService.getTraineeById(traineeId);
//
//        assertEquals(trainee, foundTrainee);
//        verify(traineeDao, times(1)).findById(traineeId);
//    }
//
//    @Test
//    void getAllTrainees_ShouldReturnStringWithAllTrainees_Test() {
//        traineeDao.save(trainee);
//        String expectedOutput = trainee.toString();
//
//        when(traineeDao.findAll()).thenReturn(expectedOutput);
//
//        String foundTrainees = traineeService.getAllTrainees();
//        System.out.println(foundTrainees);
//
//        assertEquals(expectedOutput, foundTrainees);
//        verify(traineeDao, times(1)).findAll();
//    }
//}
