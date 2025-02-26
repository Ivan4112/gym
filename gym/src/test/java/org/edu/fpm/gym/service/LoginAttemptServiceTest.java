package org.edu.fpm.gym.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {
    private LoginAttemptService loginAttemptService;

    @BeforeEach
    void setUp() {
        loginAttemptService = new LoginAttemptService();
    }

    @Test
    void loginFailed_IncreasesAttemptsAndBlocksUser_AfterMaxAttempts_Test() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        assertTrue(loginAttemptService.isBlocked(username));
    }

    @Test
    void loginFailed_DoesNotBlockUser_UnderMaxAttempts_Test() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    void isBlocked_ReturnsFalse_AfterBlockTimeExpires_Test() throws InterruptedException {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        TimeUnit.SECONDS.sleep(5);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    void loginSucceeded_RemovesUserFromBlockedList_Test() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        assertTrue(loginAttemptService.isBlocked(username));

        loginAttemptService.loginSucceeded(username);

        assertFalse(loginAttemptService.isBlocked(username));
    }

    @Test
    void loginFailed_ResetsAttempts_AfterBlocking_Test() {
        String username = "testUser";

        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);
        loginAttemptService.loginFailed(username);

        assertTrue(loginAttemptService.isBlocked(username));

        loginAttemptService.loginFailed(username);

        assertTrue(loginAttemptService.isBlocked(username));
    }

}
