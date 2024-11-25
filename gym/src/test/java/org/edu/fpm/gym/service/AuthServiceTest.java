package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.jwt.JwtUtil;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    AuthService authService;

    private final String username = "testuser";
    private final String password = "password";
    private final String encodedPassword = "$2a$10$encryptedPassword";
    private final User mockUser = TestDataFactory.createUser(username, encodedPassword);

    @Test
    public void authenticateUser_Success_Test() {
        String token = "mockJwtToken";

        when(userDetailsService.loadUserByUsername(username)).thenReturn(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password)))
                .thenReturn(authentication);

        when(jwtUtil.generateToken(username)).thenReturn(token);

        String result = authService.authenticateUser(username, password);

        assertNotNull(result);
        assertEquals(token, result);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(authenticationManager, times(1))
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        verify(jwtUtil, times(1)).generateToken(username);
    }

    @Test
    void authenticateUser_InvalidCredentials_Test() {
        when(userDetailsService.loadUserByUsername(username)).thenReturn(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.authenticateUser(username, password));

        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void authenticateUser_UnexpectedException_Test() {
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new RuntimeException("Unexpected error"));

        assertThrows(RuntimeException.class, () -> authService.authenticateUser(username, password));

        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(userRepository, never()).findByUsername(anyString());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(authenticationManager, never()).authenticate(any());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void createTrainee_Success_Test() {
        var traineeDTO = TestDataFactory.createTraineeDTO();

        when(userService.generateUsername(anyString(), anyString())).thenReturn(username);
        when(userService.generatePassword()).thenReturn(password);

        var result = authService.createTrainee(traineeDTO);

        assertNotNull(result);
        verify(traineeRepository, times(1)).save(any(Trainee.class));
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void createTrainer_Test() {
        String firstName = "John";
        String lastName = "Doe";
        var trainingType = TestDataFactory.createTrainingType();
        var trainerDTO = new TrainerDTO(
                new UserDTO(firstName, lastName, username, true), trainingType);

        var mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(username);

        var mockTrainer = mock(Trainer.class);
        when(mockTrainer.getUser()).thenReturn(mockUser);

        when(userService.generateUsername(firstName, lastName)).thenReturn(username);
        when(userService.generatePassword()).thenReturn("generatedPassword");
        when(trainerRepository.save(any(Trainer.class))).thenReturn(mockTrainer);

        var createdTrainer = authService.createTrainer(trainerDTO);

        assertNotNull(createdTrainer);
        verify(trainerRepository, times(2)).save(any(Trainer.class));
        verify(userService, times(1)).createUser(any(User.class));
    }
}
