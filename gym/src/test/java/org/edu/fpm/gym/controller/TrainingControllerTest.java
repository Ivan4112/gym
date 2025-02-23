package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.edu.fpm.gym.utils.ActionType;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingController trainingController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Test
    void addTraining_Success_Test() throws Exception {
        AddTrainingDTO addTrainingDTO = new AddTrainingDTO("john.doe", "jane.doe",
                "Yoga", LocalDate.of(2024, 11, 6), 60);

        when(trainingService.addTraining(addTrainingDTO)).thenReturn("Training added successfully");

        mockMvc.perform(post("/v1/gym/training/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(addTrainingDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void findTrainingByTrainerUsername_Success_Test() throws Exception {
        String username = "trainerUsername";
        Trainer trainer = TestDataFactory.createTrainer(TestDataFactory.createUser(username), TestDataFactory.createTrainingType());
        List<ExternalTrainingServiceDTO> trainings = List.of(
                new ExternalTrainingServiceDTO(
                        1, username,
                        "FirstName", "LastName", true,
                        LocalDate.now(), 2, ActionType.ADD));

        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);
        when(trainingService.getTrainingsByTrainer(trainer.getId())).thenReturn(ResponseEntity.ok(trainings));

        mockMvc.perform(get("/v1/gym/training/find-trainings")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].trainerFirstName").value("FirstName"));

        verify(trainerService, times(1)).getTrainerByUsername(username);
        verify(trainingService, times(1)).getTrainingsByTrainer(trainer.getId());
    }

    @Test
    void deleteTraining_Success_Test() throws Exception {
        Integer trainingId = 1;

        doNothing().when(trainingService).deleteTraining(trainingId);

        mockMvc.perform(delete("/v1/gym/training/delete")
                        .param("id_training", trainingId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Training deleted successfully."));

        verify(trainingService, times(1)).deleteTraining(trainingId);
    }

    @Test
    void deleteTraining_Conflict_Test() throws Exception {
        Integer trainingId = 1;

        doThrow(new IllegalStateException("Cannot delete training")).when(trainingService).deleteTraining(trainingId);

        mockMvc.perform(delete("/v1/gym/training/delete")
                        .param("id_training", trainingId.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Cannot delete training"));

        verify(trainingService, times(1)).deleteTraining(trainingId);
    }

    @Test
    void deleteTraining_InternalServerError_Test() throws Exception {
        Integer trainingId = 1;

        doThrow(new RuntimeException("Unexpected error")).when(trainingService).deleteTraining(trainingId);

        mockMvc.perform(delete("/v1/gym/training/delete")
                        .param("id_training", trainingId.toString()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to delete training."));

        verify(trainingService, times(1)).deleteTraining(trainingId);
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
