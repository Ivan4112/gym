package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
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
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Mock
    TrainerService trainerService;
    @InjectMocks
    TrainerController trainerController;

    private MockMvc mockMvc;
    private final String username = "john_doe";

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

        when(trainerService.getTrainerProfile(username)).thenReturn(profileDTO);

        mockMvc.perform(get("/v1/gym/trainer/profile")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.trainingType.trainingTypeName").value("Cardio"))
                .andExpect(jsonPath("$.trainees").isEmpty());
    }

    @Test
    @SneakyThrows
    void switchTrainerActivation_Success_Test() {
        boolean isActive = true;

        doNothing().when(trainerService).switchTrainerActivation(username, isActive);

        mockMvc.perform(patch("/v1/gym/trainer/status")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(content().string("Trainer status updated successfully"));
    }

    @Test
    void getMonthlyWorkload_Success_Test() throws Exception {
        String username = "testTrainer";
        TrainerWorkloadSummaryDTO summaryDTO = TestDataFactory.createTrainerWorkloadSummaryDTO(username);

        when(trainerService.getTrainerMonthlyWorkload(username)).thenReturn(summaryDTO);

        mockMvc.perform(get("/v1/gym/trainer/monthly-summary")
                        .param("username", username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(trainerService, times(1)).getTrainerMonthlyWorkload(username);
    }

    @Test
    void updateTrainerProfile_Success_Test() throws Exception {
        TrainerProfileDTO responseDTO = TestDataFactory.createTrainerProfileDTO();

        when(trainerService.updateTrainerProfile(any(TrainerUpdateProfileDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/v1/gym/trainer/profile-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "username": "username",
                                        "firstName": "firstname",
                                        "lastName": "lastname",
                                        "trainingType": {"id": 1, "name": "Cardio"},
                                        "isActive": true
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());

        verify(trainerService, times(1)).updateTrainerProfile(any(TrainerUpdateProfileDTO.class));
    }

    @Test
    void getTrainerTrainings_Success_Test() throws Exception {
        List<TrainingDTO> trainings = List.of(TestDataFactory.createTrainingDTO("Workout", "username"));

        when(trainerService.getTrainingsForTrainer(any(TrainingRequestDTO.class))).thenReturn(trainings);

        mockMvc.perform(get("/v1/gym/trainer/trainings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(trainerService, times(1)).getTrainingsForTrainer(any(TrainingRequestDTO.class));
    }
}
