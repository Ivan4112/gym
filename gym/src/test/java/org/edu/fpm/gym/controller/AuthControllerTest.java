package org.edu.fpm.gym.controller;

import lombok.SneakyThrows;
import org.edu.fpm.gym.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
        String token = "eyJhbGciOiJIUzI1NiJ9";
        String username = "testUser";
        String password = "password";

        when(authService.authenticateUser(username, password)).thenReturn(token);

        mockMvc.perform(get("/v1/gym/auth/login")
                .param("username", username)
                .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
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
