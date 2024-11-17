package org.edu.fpm.gym.controller;

import lombok.SneakyThrows;
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
    private final String username = "testUser";
    private final String password = "password";
    private final String newPassword = "newPassword";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @SneakyThrows
    void login_Success_Test() {
        when(authService.isAuthenticateUser(username, password)).thenReturn(true);
        mockMvc.perform(get("/v1/gym/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful"));
    }

    @Test
    void login_Unauthorized_Test() throws Exception {
        when(authService.isAuthenticateUser(username, password)).thenReturn(false);

        mockMvc.perform(get("/v1/gym/auth/login")
                        .param("username", username)
                        .param("password", password)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    @SneakyThrows
    void changePassword_Success_Test() {
        when(authService.changePassword(username, password, newPassword)).thenReturn(true);

        mockMvc.perform(put("/v1/gym/auth/change-password")
                        .param("username", username)
                        .param("oldPassword", password)
                        .param("newPassword", newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully"));
    }

    @Test
    @SneakyThrows
    void changePassword_Unauthorized_Test() {
        when(authService.changePassword(username, password, newPassword)).thenReturn(false);

        mockMvc.perform(put("/v1/gym/auth/change-password")
                        .param("username", username)
                        .param("oldPassword", password)
                        .param("newPassword", newPassword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid old password"));
    }

}
