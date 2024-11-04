package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edu.fpm.gym.dto.TrainerProfileDTO;
import org.edu.fpm.gym.dto.TrainingDTO;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Mock
    UserService userService;
    @Mock
    TrainerService trainerService;
    @InjectMocks
    TrainerController trainerController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainerController).build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getTrainerProfile_Success_Test() throws Exception {
        String username = "john.doe";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Run");

        TrainerProfileDTO profileDTO = new TrainerProfileDTO(
                "John", "Doe",
                trainingType, true,
                new ArrayList<>()
        );

        when(trainerService.getTrainerProfile(username)).thenReturn(profileDTO);

        mockMvc.perform(get("/gym/trainer/profile")
                        .param("username", username))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.trainingType.trainingTypeName").value("Run"))
                .andExpect(jsonPath("$.trainees").isEmpty());
    }

    @Test
    void testGetTrainerTrainings_Success() throws Exception {
        String username = "john.doe";
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("Run");

        List<TrainingDTO> trainingList = Arrays.asList(
                new TrainingDTO("Yoga", LocalDate.now(), trainingType, 60, "john.doe"),
                new TrainingDTO("Pilates", LocalDate.now(), trainingType, 45, "john.doe")
        );

        when(trainerService.getTrainingsForTrainer(username, null, null, null)).thenReturn(trainingList);

        mockMvc.perform(get("/gym/trainer/trainings")
                        .param("username", username))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$[0].trainingName").value("Yoga"))
                .andExpect(jsonPath("$[1].trainingName").value("Pilates"));
    }

    @Test
    void switchTrainerActivation_Success_Test() throws Exception {
        String username = "john.doe";
        boolean isActive = true;

        doNothing().when(trainerService).switchTrainerActivation(username, isActive);

        mockMvc.perform(patch("/gym/trainer/status")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())  // 200
                .andExpect(content().string("Trainer status updated successfully"));
    }


    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
