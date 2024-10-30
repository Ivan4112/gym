package org.edu.fpm.gym.security;

import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityCredentialTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SecurityCredential securityCredential;

    @Test
    void generateUsernameWhenUserDoesNotExist_Test() {
        String firstName = "John";
        String lastName = "Doe";

        when(userService.existsUserByUsername("John.Doe")).thenReturn(false);

        String result = securityCredential.generateUsername(firstName, lastName);
        assertEquals("John.Doe", result);
    }

    @Test
    void generateUsernameWhenUserAlreadyExist_Test() {
        String firstName = "John";
        String lastName = "Doe";

        when(userService.existsUserByUsername("John.Doe")).thenReturn(true);
        when(userService.existsUserByUsername("John.Doe1")).thenReturn(false);

        String result = securityCredential.generateUsername(firstName, lastName);
        assertEquals("John.Doe1", result);
    }

    @Test
    void generatePassword() {
        int expectedLength = 10;
        assertEquals(expectedLength, securityCredential.generatePassword().length());
    }

    @Test
    void updatePasswordWhenUserExists_Test() {
        String username = "john.doe";
        String newPassword = "newPassword123";

        User existingUser = new User();
        existingUser.setUsername(username);

        when(userService.findUserByUsername(username)).thenReturn(existingUser);

        securityCredential.updatePassword(username, newPassword);

        verify(userService).updateUser(existingUser);
        assertEquals(newPassword, existingUser.getPassword());
    }

    @Test
    void updatePasswordWhenUserDoesNotExist_Test() {
        String username = "unknown.user";
        String newPassword = "newPassword123";

        when(userService.findUserByUsername(username)).thenReturn(null);

        securityCredential.updatePassword(username, newPassword);

        verify(userService, never()).updateUser(any(User.class));
    }
}
