package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.security.SecurityCredential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private SecurityCredential securityCredential;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

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
        String username = "John.Doe";
        String password = "password123";
        when(securityCredential.generateUsername(trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getId())).thenReturn(username);
        when(securityCredential.generatePassword()).thenReturn(password);
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        verify(securityCredential).generateUsername(trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getId());
        verify(securityCredential).generatePassword();
        verify(trainerRepository).save(trainer);
        assertEquals(username, trainer.getUser().getUsername());
        assertEquals(password, trainer.getUser().getPassword());
        assertEquals(createdTrainer, trainer);
    }

    @Test
    void getTrainerByUsername_Test() {
        String username = "John.Doe";

        when(trainerRepository.findByUsername(username)).thenReturn(trainer);
        Trainer foundTrainer = trainerService.getTrainerByUsername(username);
        verify(trainerRepository).findByUsername(username);
        assertEquals(trainer, foundTrainer);
    }

    @Test
    void updateTrainerProfile_Test() {
        trainerService.updateTrainerProfile(trainer);
        verify(trainerRepository).updateTrainer(trainer);
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
        verify(trainerRepository).deleteByUsername(username);
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
    void existsByFullName_Test() {
        String firstName = "John";
        String lastName = "Doe";
        when(trainerRepository.findByFirstNameAndLastName(firstName, lastName)).thenReturn(Optional.of(trainer));

        boolean exists = trainerService.existsByFullName(firstName, lastName);

        verify(trainerRepository).findByFirstNameAndLastName(firstName, lastName);
        assertTrue(exists);
    }
}
