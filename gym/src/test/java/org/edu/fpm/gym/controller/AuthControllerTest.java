package org.edu.fpm.gym.controller;

import org.edu.fpm.gym.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void login_Success_Test() throws Exception {
        String username = "testUser";
        String password = "testPassword";

        when(authService.authenticateUser(username, password)).thenReturn(true);

        mockMvc.perform(get("/gym/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_Unauthorized_Test() throws Exception {
        String username = "testUser";
        String password = "wrongPassword";

        when(authService.authenticateUser(username, password)).thenReturn(false);

        mockMvc.perform(get("/gym/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    void changePassword_Success_Test() throws Exception {
        String username = "testUser";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        when(authService.changePassword(username, oldPassword, newPassword)).thenReturn(true);

        mockMvc.perform(put("/gym/auth/change-password")
                        .param("username", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    void changePassword_Unauthorized_Test() throws Exception {
        String username = "testUser";
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword";

        when(authService.changePassword(username, oldPassword, newPassword)).thenReturn(false);

        mockMvc.perform(put("/gym/auth/change-password")
                        .param("username", username)
                        .param("oldPassword", oldPassword)
                        .param("newPassword", newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid old password"));
    }

}
