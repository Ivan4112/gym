package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
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

    @Test
    void authenticateUser_Success_Test() {
        String username = "testuser";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean isAuthenticated = authService.authenticateUser(username, password);

        assertTrue(isAuthenticated);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void authenticateUser_InvalidPassword_Test() {
        String username = "testuser";
        String password = "password";
        String wrongPassword = "wrongpassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean isAuthenticated = authService.authenticateUser(username, wrongPassword);

        assertFalse(isAuthenticated);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void authenticateUser_UserNotFound_Test() {
        String username = "nonexistentuser";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean isAuthenticated = authService.authenticateUser(username, password);

        assertFalse(isAuthenticated);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void changePassword_Success_Test() {
        String username = "testuser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(oldPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean passwordChanged = authService.changePassword(username, oldPassword, newPassword);

        assertTrue(passwordChanged);
        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void changePassword_InvalidOldPassword_Test() {
        String username = "testuser";
        String oldPassword = "oldPassword";
        String wrongOldPassword = "wrongOldPassword";
        String newPassword = "newPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(oldPassword);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean passwordChanged = authService.changePassword(username, wrongOldPassword, newPassword);

        assertFalse(passwordChanged);
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void changePassword_UserNotFound_Test() {
        String username = "nonexistentuser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean passwordChanged = authService.changePassword(username, oldPassword, newPassword);

        assertFalse(passwordChanged);
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(0)).save(any(User.class));
    }
}
