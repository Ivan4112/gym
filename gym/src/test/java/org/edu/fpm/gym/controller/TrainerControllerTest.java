package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Mock
    TrainerService trainerService;
    @InjectMocks
    TrainerController trainerController;

    private MockMvc mockMvc;
    private final String username = "john_doe";
    private final String password = "password";
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    void getTrainerProfile_Success_Test() {
        TrainerProfileDTO profileDTO = TestDataFactory.createTrainerProfileDTO();

        when(trainerService.getTrainerProfile(username, password)).thenReturn(profileDTO);

        mockMvc.perform(get("/v1/gym/trainer/profile")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.trainingType.trainingTypeName").value("Cardio"))
                .andExpect(jsonPath("$.trainees").isEmpty());
    }

    @Test
    @SneakyThrows
    void testGetTrainerTrainings_Success() {
        List<TrainingDTO> trainingList = TestDataFactory.createTrainingDTOList();
        var requestDTO = new TrainingRequestDTO(username, null, null, null, null, password);

        when(trainerService.getTrainingsForTrainer(requestDTO)).thenReturn(trainingList);

        mockMvc.perform(get("/v1/gym/trainer/trainings")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("Yoga"))
                .andExpect(jsonPath("$[1].trainingName").value("Pilates"));
    }

    @Test
    @SneakyThrows
    void switchTrainerActivation_Success_Test() {
        boolean isActive = true;

        doNothing().when(trainerService).switchTrainerActivation(username, isActive, password);

        mockMvc.perform(patch("/v1/gym/trainer/status")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive))
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Trainer status updated successfully"));
    }
}
