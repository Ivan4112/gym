package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.TraineeProfileDTO;
import org.edu.fpm.gym.dto.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.TrainerDTO;
import org.edu.fpm.gym.dto.TrainingDTO;
import org.edu.fpm.gym.entity.*;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    TrainerService trainerService;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    public void setUp() {

        trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setFirstName("John");
        trainee.getUser().setLastName("Doe");
        trainee.getUser().setUsername("john.doe");
        trainee.getUser().setPassword("testPassword");
        trainee.getUser().setIsActive(true);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("testAddress");
    }
    @Test
    void createTrainee_Test() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("traineeUsername");
        trainee.setUser(user);

        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.createTrainee(trainee);

        assertNotNull(result);
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void getTraineeByUsername_Test() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        Trainee result = traineeService.getTraineeByUsername(username);

        assertNotNull(result);
        assertEquals(trainee, result);
        verify(traineeRepository, times(1)).findTraineeByUser_Username(username);
    }

    @Test
    void deleteTraineeByUsername_Test() {
        String username = "testuser";

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUsername(username);

        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        traineeService.deleteTraineeByUsername(username);

        verify(traineeRepository, times(1)).delete(trainee);
    }

    @Test
    void switchTraineeActivation_Test() {
        String username = "testuser";
        boolean isActive = true;

        traineeService.switchTraineeActivation(username, isActive);

        verify(traineeRepository, times(1)).switchActivation(username, isActive);
    }

    @Test
    void getTraineeTrainings_Test() {
        String username = "testuser";
        LocalDate fromDate = LocalDate.of(2023, 1, 1);
        LocalDate toDate = LocalDate.of(2023, 12, 31);
        String trainerName = "trainer";
        TrainingType trainingType = new TrainingType();

        List<Training> trainings = new ArrayList<>();
        Training training = new Training();
        training.setTrainingName("Cardio Workout");
        training.setTrainingDate(LocalDate.of(2023, 5, 1));
        training.setTrainingType(trainingType);
        Trainer trainer = new Trainer();
        User trainerUser = new User();
        trainerUser.setFirstName("John");
        trainer.setUser(trainerUser);
        training.setTrainer(trainer);
        trainings.add(training);

        when(traineeRepository.findTrainingsByTraineeAndDateRange(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(trainings);

        List<TrainingDTO> result = traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Cardio Workout", result.get(0).getTrainingName());
        assertEquals(LocalDate.of(2023, 5, 1), result.get(0).getTrainingDate());
        assertEquals("John", result.get(0).getUserName());
    }

    @Test
    void updateTraineeTrainers_Test() {
        String username = "testuser";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");

        Trainee trainee = new Trainee();
        trainee.setUser(new User());
        trainee.getUser().setUsername(username);

        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        trainer1.setUser(new User("trainer1", "John", "Doe", true));
        trainer2.setUser(new User("trainer2", "Jane", "Smith", true));

        when(trainerService.getTrainerByUsername("trainer1")).thenReturn(trainer1);
        when(trainerService.getTrainerByUsername("trainer2")).thenReturn(trainer2);
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        List<TrainerDTO> result = traineeService.updateTraineeTrainers(username, trainerUsernames);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(traineeRepository, times(1)).save(trainee);
    }

    @Test
    void getTraineeProfile_TraineeExists_Test() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(username);
        user.setIsActive(true);
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.of(1990, 1, 1));
        trainee.setAddress("123 Street");

        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        TraineeProfileDTO result = traineeService.getTraineeProfile(username);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123 Street", result.getAddress());
    }

    @Test
    void getTraineeProfile_TraineeNotFound_Test() {
        String username = "unknownuser";

        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(null);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            traineeService.getTraineeProfile(username);
        });

        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void updateTraineeProfile_TraineeExists_Test() {
        TraineeUpdateProfileDTO updateProfile = new TraineeUpdateProfileDTO();
        updateProfile.setUsername("testuser");
        updateProfile.setFirstName("UpdatedFirstName");
        updateProfile.setLastName("UpdatedLastName");
        updateProfile.setActive(true);
        updateProfile.setDateOfBirth(LocalDate.of(1995, 5, 5));
        updateProfile.setAddress("Updated Address");

        Trainee trainee = new Trainee();
        User user = new User();
        user.setUsername("testuser");
        trainee.setUser(user);

        when(traineeRepository.findTraineeByUser_Username("testuser")).thenReturn(trainee);

        TraineeProfileDTO result = traineeService.updateTraineeProfile(updateProfile);

        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("Updated Address", result.getAddress());
        verify(traineeRepository, times(1)).updateTraineeByUserUsername(eq("testuser"), any(Trainee.class));
    }
}
