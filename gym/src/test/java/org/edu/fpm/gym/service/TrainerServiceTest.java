package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {

        trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setFirstName("John");
        trainer.getUser().setLastName("Doe");
        trainer.getUser().setUsername("john.doe");
        trainer.getUser().setPassword("testPassword");
        trainer.getUser().setIsActive(true);
        trainer.setSpecialization(new TrainingType());
        trainer.setTrainees(new HashSet<>());

    }

    @Test
    void createTrainer_Test() {
        String username = "john.doe";
        String password = "testPassword";
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        verify(trainerRepository).save(trainer);
        assertEquals(username, trainer.getUser().getUsername());
        assertEquals(password, trainer.getUser().getPassword());
        assertEquals(createdTrainer, trainer);
    }

    @Test
    void getTrainerByUsername_Test() {
        String username = "John.Doe";

        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);
        Trainer foundTrainer = trainerService.getTrainerByUsername(username);
        verify(trainerRepository).findTrainerByUser_Username(username);
        assertEquals(trainer, foundTrainer);
    }

    @Test
    void updateTrainerProfile_Test() {
        trainerService.updateTrainerProfile(trainer);
        verify(trainerRepository).updateTrainerByUserUsername(trainer);
    }

    @Test
    void changeTrainerPassword_Test() {
        String username = "John.Doe";
        String newPassword = "newPassword";

        trainerService.changeTrainerPassword(username, newPassword);
        verify(trainerRepository).changePassword(username, newPassword);
    }

    @Test
    void switchTrainerActivation_Test() {
        String username = "John.Doe";

        trainerService.switchTrainerActivation(username);
        verify(trainerRepository).switchActivation(username);
    }

    @Test
    void deleteTrainer_Test() {
        String username = "John.Doe";

        trainerService.deleteTrainer(username);
        verify(trainerRepository).deleteTrainerByUser_Username(username);
    }

    @Test
    void getAvailableTrainersForTrainee_Test() {
        String traineeUsername = "traineeUsername";
        when(trainerRepository.findAvailableTrainersForTrainee(traineeUsername)).thenReturn(Collections.singletonList(trainer));

        List<Trainer> availableTrainers = trainerService.getAvailableTrainersForTrainee(traineeUsername);

        verify(trainerRepository).findAvailableTrainersForTrainee(traineeUsername);
        assertEquals(1, availableTrainers.size());
        assertEquals(trainer, availableTrainers.get(0));
    }

    @Test
    void getTrainingsForTrainer_Test() {
        String username = "trainer.username";
        LocalDate fromDate = LocalDate.now().minusDays(7);
        LocalDate toDate = LocalDate.now();
        String traineeName = "trainee.username";

        Training training1 = new Training();
        Training training2 = new Training();
        List<Training> expectedTrainings = List.of(training1, training2);

        when(trainerRepository.getTrainingsForTrainer(username, fromDate, toDate, traineeName))
                .thenReturn(expectedTrainings);

        List<Training> actualTrainings = trainerService.getTrainingsForTrainer(username, fromDate, toDate, traineeName);

        assertNotNull(actualTrainings);
        assertEquals(expectedTrainings.size(), actualTrainings.size());
        assertEquals(expectedTrainings, actualTrainings);
    }
}
