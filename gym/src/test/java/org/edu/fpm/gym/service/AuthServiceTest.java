package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @InjectMocks
    AuthService authService;

    private final String username = "testuser";
    private final String password = "password";
    private final String anotherPassword = "newPassword";
    private final User user = TestDataFactory.createUser("testuser");


    @Test
    void isAuthenticateUser_Success_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertTrue(authService.isAuthenticateUser(username, password));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void isAuthenticateUser_InvalidPassword_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertFalse(authService.isAuthenticateUser(username, anotherPassword));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void isAuthenticateUser_UserNotFound_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertFalse(authService.isAuthenticateUser(username, password));
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void changePassword_Success_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertTrue(authService.changePassword(username, password, anotherPassword));
        assertEquals(anotherPassword, user.getPassword());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_InvalidOldPassword_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        assertFalse(authService.changePassword(username, anotherPassword, password));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void changePassword_UserNotFound_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertFalse(authService.changePassword(username, password, anotherPassword));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(0)).save(any(User.class));
    }
}
