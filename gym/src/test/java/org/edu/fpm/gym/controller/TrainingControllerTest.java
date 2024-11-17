package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edu.fpm.gym.dto.AddTrainingDTO;
import org.edu.fpm.gym.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void addTraining_Success() throws Exception {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO();
        addTrainingDTO.setTraineeUsername("john.doe");
        addTrainingDTO.setTrainerUsername("jane.doe");
        addTrainingDTO.setTrainingName("Yoga");
        addTrainingDTO.setTrainingDate(LocalDate.of(2024, 11, 6));
        addTrainingDTO.setTrainingDuration(60);

        when(trainingService.addTraining(
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                any(Integer.class)
        )).thenReturn(true);

        mockMvc.perform(post("/gym/training/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addTrainingDTO)))
                .andExpect(status().isOk());  // 200
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addTraining_Failure() throws Exception {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO();
        addTrainingDTO.setTraineeUsername("john.doe");
        addTrainingDTO.setTrainerUsername("jane.doe");
        addTrainingDTO.setTrainingName("Yoga");
        addTrainingDTO.setTrainingDate(LocalDate.of(2024, 11, 6));
        addTrainingDTO.setTrainingDuration(60);

        when(trainingService.addTraining(
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                any(Integer.class)
        )).thenReturn(false);

        mockMvc.perform(post("/gym/training/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addTrainingDTO)))
                .andExpect(status().isBadRequest()); // 400
    }
}
