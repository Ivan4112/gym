package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private String username;
    private String newPassword;

    @BeforeEach
    public void setUp() {
        username = "john.doe";
        newPassword = "newpassword123";
        user = TestDataFactory.createUser("john.doe");
    }



    @Test
    void createUser_Test() {
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void generateUsername_NewUsername_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        var generatedUsername = userService.generateUsername("john", "doe");
        assertEquals(username, generatedUsername);
    }

    @Test
    void generateUsername_ExistingUsername_Test() {
        var specialUser = TestDataFactory.createUser("john.doe1");
        String expectedUsername = "john.doe1";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(specialUser));
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());
        var generatedUsername = userService.generateUsername("john", "doe");
        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    void updateUserPassword_UserExists_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.updateUserPassword(username, newPassword);
        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void updateUserPassword_UserNotFound_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        userService.updateUserPassword(username, newPassword);
        verify(userRepository, never()).saveAndFlush(user);
    }

    @Test
    void generatePassword_Test() {
        String generatedPassword = userService.generatePassword();
        assertNotNull(generatedPassword);
        assertEquals(10, generatedPassword.length());
    }

    @Test
    void existsUserByUsername_UserExists_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        boolean exists = userService.existsUserByUsername(username);
        assertTrue(exists);
    }
    @Test
    void existsUserByUsername_UserDoesNotExist_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        boolean exists = userService.existsUserByUsername(username);
        assertFalse(exists);
    }
    @Test
    void findUserByUsername_UserExists_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        var foundUser = userService.findUserByUsername(username);
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void findUserByUsername_UserDoesNotExist_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertNull(userService.findUserByUsername(username));
    }

    @Test
    void updateUser_Test() {
        userService.updateUser(user);
        verify(userRepository).saveAndFlush(user);
    }
}
