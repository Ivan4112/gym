package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.edu.fpm.gym.dto.trainee.TraineeProfileDTO;
import org.edu.fpm.gym.dto.trainee.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TraineeController traineeController;

    private ObjectMapper objectMapper;
    private final String username = "john.doe";
    private final String password = "password";
    private TraineeProfileDTO traineeProfileDTO;
    @BeforeEach
    void setUp() {
        traineeProfileDTO = new TraineeProfileDTO("John", "Doe", "1990-01-01", "123 Main St", true, new ArrayList<>());

        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @SneakyThrows
    void registerTrainee_Test() {
        String jsonContent = Files.readString(Paths.get(getClass().getClassLoader().getResource("data/registerTrainee.json").toURI()));

        mockMvc.perform(post("/v1/gym/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    void getTraineeProfile_Success_Test() {
        when(traineeService.getTraineeProfile("john.doe", "password")).thenReturn(traineeProfileDTO);

        mockMvc.perform(get("/v1/gym/trainee/profile")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    @SneakyThrows
    void updateTraineeProfile_Success_Test() {
        var updateRequest =
                new TraineeUpdateProfileDTO("john_doe", "John",
                        "Doe", LocalDate.of(1990, 1, 1), "123 Main St", true);

        when(traineeService.updateTraineeProfile(updateRequest, password))
                .thenReturn(traineeProfileDTO);

        mockMvc.perform(put("/v1/gym/trainee/profile-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }


    @Test
    @SneakyThrows
    void deleteTraineeProfile_Success_Test() {
        doNothing().when(traineeService).deleteTraineeByUsername(username, password);

        mockMvc.perform(delete("/v1/gym/trainee/profile-delete")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted"));
    }

    @Test
    @SneakyThrows
    void getNotAssignedActiveTrainers_Success_Test() {
        List<Trainer> trainers = TestDataFactory.createTrainerList();

        when(trainerService.getAvailableTrainersForTrainee(username, password)).thenReturn(trainers);

        mockMvc.perform(get("/v1/gym/trainee/unassigned-trainers")
                        .param("username", username)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("John.Doe"))
                .andExpect(jsonPath("$[1].user.username").value("Jane.Smith"))
                .andExpect(jsonPath("$[0].specialization.trainingTypeName").value("Cardio"))
                .andExpect(jsonPath("$[1].specialization.trainingTypeName").value("Cardio"));
    }

    @Test
    @SneakyThrows
    void updateTraineeTrainers_Success_Test() {
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");

        var trainer1 = new TrainerDTO(new UserDTO("John", "Doe", "trainer1", true), TestDataFactory.createTrainingType());
        var trainer2 = new TrainerDTO(new UserDTO("Jane", "Smith", "trainer2", true), TestDataFactory.createTrainingType());

        List<TrainerDTO> updatedTrainers = Arrays.asList(trainer1, trainer2);

        when(traineeService.updateTraineeTrainers(username, trainerUsernames, password)).thenReturn(updatedTrainers);

        mockMvc.perform(put("/v1/gym/trainee/trainers?username=" + username + "&password="+password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("trainer1"))
                .andExpect(jsonPath("$[1].user.username").value("trainer2"))
                .andExpect(jsonPath("$[0].specialization.trainingTypeName").value("Cardio"))
                .andExpect(jsonPath("$[1].specialization.trainingTypeName").value("Cardio"));
    }

    @Test
    @SneakyThrows
    void updateTraineeStatus_Success_Test() {
        boolean isActive = true;

        doNothing().when(traineeService).switchTraineeActivation(username, isActive, password);

        mockMvc.perform(patch("/v1/gym/trainee/status")
                        .param("username", username)
                        .param("password", password)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Trainee status updated successfully"));
    }
}
