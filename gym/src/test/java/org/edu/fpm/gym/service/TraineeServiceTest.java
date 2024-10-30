package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;

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
        String username = "john.doe";
        String password = "testPassword";

        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        verify(traineeRepository).save(trainee);
        assertEquals(username, trainee.getUser().getUsername());
        assertEquals(password, trainee.getUser().getPassword());
        assertEquals(createdTrainee, trainee);
    }

    @Test
    void getTraineeByUsername_Test() {
        String username = "Jane.Doe";
        when(traineeRepository.findTraineeByUser_Username(username)).thenReturn(trainee);

        Trainee foundTrainee = traineeService.getTraineeByUsername(username);

        verify(traineeRepository).findTraineeByUser_Username(username);
        assertEquals(trainee, foundTrainee);
    }

    @Test
    void updateTraineeProfile_Test() {
        traineeService.updateTraineeProfile(trainee);
        verify(traineeRepository).updateTraineeByUserUsername(trainee);
    }

    @Test
    void deleteTraineeByUsername_Test() {
        String username = "Jane.Doe";

        traineeService.deleteTraineeByUsername(username);
        verify(traineeRepository).deleteTraineeByUser_Username(username);
    }

    @Test
    void changeTraineePassword_Test() {
        String username = "Jane.Doe";
        String newPassword = "newPassword";

        traineeService.changeTraineePassword(username, newPassword);
        verify(traineeRepository).changePassword(username, newPassword);
    }

    @Test
    void switchTraineeActivation_Test() {
        String username = "Jane.Doe";

        traineeService.switchTraineeActivation(username);
        verify(traineeRepository).switchActivation(username);
    }

    @Test
    void getTraineeTrainings_Test() {
        String username = "Jane.Doe";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now().plusDays(7);
        String trainerName = "TrainerName";
        TrainingType trainingType = new TrainingType();
        when(traineeRepository.findTrainingsByTraineeAndDateRange(username, fromDate, toDate, trainerName, trainingType))
                .thenReturn(Collections.singletonList(new Training()));

        List<Training> trainings = traineeService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        verify(traineeRepository).findTrainingsByTraineeAndDateRange(username, fromDate, toDate, trainerName, trainingType);
        assertEquals(1, trainings.size());
    }
}
