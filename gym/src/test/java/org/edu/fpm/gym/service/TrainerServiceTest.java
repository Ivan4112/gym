package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.TrainerProfileDTO;
import org.edu.fpm.gym.dto.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.TrainingDTO;
import org.edu.fpm.gym.entity.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");

        trainer = new Trainer();
        trainer.setUser(new User());
        trainer.getUser().setFirstName("John");
        trainer.getUser().setLastName("Doe");
        trainer.getUser().setUsername("john.doe");
        trainer.getUser().setPassword("testPassword");
        trainer.getUser().setIsActive(true);
        trainer.setSpecialization(trainingType);
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
    void switchTrainerActivation_Test() {
        String username = "johndoe";
        boolean isActive = true;

        trainerService.switchTrainerActivation(username, isActive);

        verify(trainerRepository, times(1)).switchActivation(username, isActive);
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
        String username = "johndoe";
        LocalDate fromDate = LocalDate.now().minusDays(1);
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Cardio");
        LocalDate toDate = LocalDate.now();

        String traineeName = "Trainee";
        Training training = new Training();
        training.setTrainingName("Cardio");
        training.setTrainingDate(LocalDate.now());
        training.setTrainingType(trainingType);
        training.setTrainingDuration(60);

        Trainee trainee = new Trainee();
        trainee.setUser(new User("Trainee", "John", "Doe", true));

        training.setTrainee(trainee);
        when(trainerRepository.getTrainingsForTrainer(username, fromDate, toDate, traineeName))
                .thenReturn(List.of(training));

        List<TrainingDTO> trainings = trainerService.getTrainingsForTrainer(username, fromDate, toDate, traineeName);

        assertNotNull(trainings);
        assertEquals(1, trainings.size());
        assertEquals("Cardio", trainings.get(0).getTrainingName());
        assertEquals("John", trainings.get(0).getUserName());
    }

    @Test
    void getTrainerProfile_Test() {
        String username = "johndoe";

        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(trainer);

        TrainerProfileDTO profile = trainerService.getTrainerProfile(username);

        assertNotNull(profile);
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertEquals("Yoga", profile.getTrainingType().getTrainingTypeName());
    }

    @Test
    void updateTrainerProfile_Test() {
        TrainerUpdateProfileDTO updateProfile = new TrainerUpdateProfileDTO();
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Yoga");

        updateProfile.setUsername("johndoe");
        updateProfile.setFirstName("UpdatedJohn");
        updateProfile.setLastName("UpdatedDoe");
        updateProfile.setTrainingType(trainingType);
        updateProfile.setActive(true);

        when(trainerRepository.findTrainerByUser_Username("johndoe")).thenReturn(trainer);
        when(trainerRepository.updateTrainerByUserUsername(anyString(), any(Trainer.class))).thenReturn(trainer);

        TrainerProfileDTO updatedProfile = trainerService.updateTrainerProfile(updateProfile);

        assertNotNull(updatedProfile);
        assertEquals("UpdatedJohn", updatedProfile.getFirstName());
        assertEquals("UpdatedDoe", updatedProfile.getLastName());
        assertEquals("Yoga", updatedProfile.getTrainingType().getTrainingTypeName());
    }

    @Test
    void getTrainerProfile_TrainerNotFound_Test() {
        String username = "nonexistent";
        when(trainerRepository.findTrainerByUser_Username(username)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> trainerService.getTrainerProfile(username));
        assertEquals("Trainee not found", exception.getMessage());
    }

    @Test
    void updateTrainerProfile_TrainerNotFound_Test() {
        TrainerUpdateProfileDTO updateProfile = new TrainerUpdateProfileDTO();
        updateProfile.setUsername("nonexistent");

        when(trainerRepository.findTrainerByUser_Username("nonexistent")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> trainerService.updateTrainerProfile(updateProfile));
        assertEquals("Trainer not found", exception.getMessage());
    }
}
