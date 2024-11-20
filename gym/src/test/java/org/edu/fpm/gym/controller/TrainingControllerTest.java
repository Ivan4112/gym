package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
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
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO("john.doe", "jane.doe",
                "Yoga", LocalDate.of(2024, 11, 6), 60);

        when(trainingService.addTraining(addTrainingDTO)).thenReturn("Training added successfully");

        mockMvc.perform(post("/v1/gym/training/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addTrainingDTO)))
                .andExpect(status().isCreated());
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
}
