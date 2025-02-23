package org.edu.fpm.gym.controller;

import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.auth.LoginRequest;
import org.edu.fpm.gym.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
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
    @SneakyThrows
    void login_Success_Test() {
        String username = "testUser";
        String password = "testPassword";
        String expectedToken = "mockToken";
        LoginRequest loginRequest = new LoginRequest(username, password);

        when(authService.authenticateUser(username, password)).thenReturn(expectedToken);

        ResponseEntity<String> response = authController.login(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedToken, response.getBody());
        verify(authService, times(1)).authenticateUser(username, password);
    }

    @Test
    @SneakyThrows
    void registerTrainee_Test() {
        String jsonContent = Files.readString(Paths.get(getClass().getClassLoader().getResource("data/registerTrainee.json").toURI()));

        mockMvc.perform(post("/v1/gym/auth/register/trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    void registerTrainer_Test() throws Exception {
        String jsonContent = Files.readString(Paths.get(getClass().getClassLoader().getResource("data/registerTrainer.json").toURI()));

        mockMvc.perform(post("/v1/gym/auth/register/trainer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }
}
