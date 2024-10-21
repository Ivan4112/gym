//package org.edu.fpm.gym.security;
//
//import org.edu.fpm.gym.storage.TraineeStorage;
//import org.edu.fpm.gym.storage.TrainerStorage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//class SecurityCredentialTest {
//
//    @Mock
//    TrainerStorage trainerStorage;
//    @Mock
//    TraineeStorage traineeStorage;
//    @InjectMocks
//    SecurityCredential securityCredential;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void generateUsername_ShouldReturnUserNameByConcatenatingNameAndLastName_Test() {
//        when(traineeStorage.getTrainees()).thenReturn(new HashMap<>());
//        when(trainerStorage.getTrainers()).thenReturn(new HashMap<>());
//
//        String username = securityCredential.generateUsername("John", "Doe", 1L);
//        assertEquals("John.Doe", username);
//    }
//
//    @Test
//    public void generateUsername_ShouldReturnUniqUsernameIfSuchUsernameAlreadyExisting_Test() {
//        Map<Long, Trainee> trainees = new HashMap<>();
//        Trainee existingTrainee = new Trainee(1L, "John", "Doe", "john.doe", "pass",
//                true, LocalDate.of(2000, 1, 1), "123 Main St");
//        trainees.put(1L, existingTrainee);
//
//        when(traineeStorage.getTrainees()).thenReturn(trainees);
//        when(trainerStorage.getTrainers()).thenReturn(new HashMap<>());
//
//        String username = securityCredential.generateUsername("John", "Doe", 2L);
//        assertEquals("John.Doe2", username);
//    }
//
//    @Test
//    void generatePassword_Test() {
//        String password = securityCredential.generatePassword();
//        assertNotNull(password);
//        assertEquals(10, password.length());
//    }
//}
