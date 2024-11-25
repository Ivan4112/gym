package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User user;
    private final String username = "john.doe";
    private final String newPassword = "newpassword123";
    private final String encodedPassword = "$2a$10$encryptedPassword";

    @BeforeEach
    public void setUp() {
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
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        userService.updateUserPassword(username, newPassword);
        assertEquals(encodedPassword, user.getPassword());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    void updateUserPassword_UserNotFound_Test() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        userService.updateUserPassword(username, newPassword);
        verify(userRepository, never()).save(any());
    }

    @Test
    void generatePassword_Test() {
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        try (MockedStatic<RandomStringUtils> mockedRandomStringUtils = mockStatic(RandomStringUtils.class)) {
            mockedRandomStringUtils.when(() -> RandomStringUtils.randomAlphanumeric(10)).thenReturn(newPassword);

            String result = userService.generatePassword();

            verify(passwordEncoder).encode(newPassword);
            assertEquals(encodedPassword, result);
        }
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
