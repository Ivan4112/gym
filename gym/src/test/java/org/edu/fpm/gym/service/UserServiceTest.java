package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
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

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");
        user.setPassword("testPassword");
    }
    @Test
    void createUser_Test() {
        userService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void generateUsername_NewUsername_Test() {
        String firstName = "John";
        String lastName = "Doe";
        String expectedUsername = "John.Doe";

        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        String generatedUsername = userService.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    void generateUsername_ExistingUsername_Test() {
        String firstName = "John";
        String lastName = "Doe";
        String baseUsername = "John.Doe";
        String expectedUsername = "John.Doe1";

        when(userRepository.findByUsername(baseUsername)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(expectedUsername)).thenReturn(Optional.empty());

        String generatedUsername = userService.generateUsername(firstName, lastName);

        assertEquals(expectedUsername, generatedUsername);
    }

    @Test
    void updateUserPassword_UserExists_Test() {
        String username = "john.doe";
        String newPassword = "newpassword123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.updateUserPassword(username, newPassword);

        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void updateUserPassword_UserNotFound_Test() {
        String username = "john.doe";
        String newPassword = "newpassword123";

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
        String username = "john.doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        boolean exists = userService.existsUserByUsername(username);

        assertTrue(exists);
    }
    @Test
    void existsUserByUsername_UserDoesNotExist_Test() {
        String username = "john.doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        boolean exists = userService.existsUserByUsername(username);

        assertFalse(exists);
    }
    @Test
    void findUserByUsername_UserExists_Test() {
        String username = "john.doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByUsername(username);

        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }

    @Test
    void findUserByUsername_UserDoesNotExist_Test() {
        String username = "john.doe";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        User foundUser = userService.findUserByUsername(username);

        assertNull(foundUser);
    }

    @Test
    void updateUser_Test() {
        userService.updateUser(user);

        verify(userRepository).saveAndFlush(user);
    }
}
