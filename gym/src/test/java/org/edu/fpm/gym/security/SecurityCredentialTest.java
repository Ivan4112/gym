package org.edu.fpm.gym.security;

import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SecurityCredentialTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private SecurityCredential securityCredential;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUsernameWhenUserDoesNotExist_Test() {
        String firstName = "John";
        String lastName = "Doe";
        Integer id = 1;

        when(traineeService.existsByFullName(firstName, lastName)).thenReturn(false);
        when(trainerService.existsByFullName(firstName, lastName)).thenReturn(false);

        String result = securityCredential.generateUsername(firstName, lastName, id);
        assertEquals("John.Doe", result);
    }

    @Test
    void generateUsernameWhenUserAlreadyExist_Test() {
        String firstName = "John";
        String lastName = "Doe";
        Integer id = 1;

        when(traineeService.existsByFullName(firstName, lastName)).thenReturn(true);
        when(trainerService.existsByFullName(firstName, lastName)).thenReturn(false);

        String result = securityCredential.generateUsername(firstName, lastName, id);
        assertEquals("John.Doe1", result);
    }

    @Test
    void generatePassword() {

    }
}
