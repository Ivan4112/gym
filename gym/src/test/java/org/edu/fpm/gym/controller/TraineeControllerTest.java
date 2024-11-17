package org.edu.fpm.gym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.edu.fpm.gym.dto.*;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.exception.UnauthorizedException;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.UserService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeController traineeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void registerTrainee_Test() throws Exception {
        TraineeDTO traineeDTO = new TraineeDTO();
        traineeDTO.setUser(new UserDTO("John", "Doe", "john.doe", true));
        traineeDTO.setAddress("123 Main St");
        traineeDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));

        String jsonContent = Files.readString(Paths.get(getClass().getClassLoader().getResource("data/registerTrainee.json").toURI()));


        when(userService.generatePassword()).thenReturn("randomPassword");
        when(userService.generateUsername("John", "Doe")).thenReturn("John.Doe");

        mockMvc.perform(post("/gym/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void getTraineeProfile_Success_Test() throws Exception {
        TraineeProfileDTO profileDTO = new TraineeProfileDTO();
        profileDTO.setFirstName("John");
        profileDTO.setLastName("Doe");
        profileDTO.setDateOfBirth("1990-01-01");
        profileDTO.setAddress("123 Main St");
        profileDTO.setActive(true);

        when(traineeService.getTraineeProfile("John.Doe")).thenReturn(profileDTO);

        mockMvc.perform(get("/gym/trainee/profile")
                        .param("username", "John.Doe"))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
    }

    @Test
    void getTraineeProfile_NotFound_Test() throws Exception {
        when(traineeService.getTraineeProfile("John.Doe")).thenThrow(new RuntimeException("Trainee not found"));

        mockMvc.perform(get("/gym/trainee/profile")
                        .param("username", "John.Doe"))
                .andExpect(status().isNotFound())  // 404
                .andExpect(jsonPath("$.message", is("Trainee not found")));
    }

    @Test
    void getTraineeProfile_Unauthorized_Test() throws Exception {
        when(traineeService.getTraineeProfile("John.Doe")).thenThrow(new UnauthorizedException("User unauthorized"));

        mockMvc.perform(get("/gym/trainee/profile")
                        .param("username", "John.Doe"))
                .andExpect(status().isUnauthorized());  // 401
    }

    @Test
    void deleteTraineeProfile_Test() throws Exception {
        mockMvc.perform(delete("/gym/trainee/profile-delete")
                        .param("username", "John.Doe"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTraineeProfile_Success_Test() throws Exception {
        TraineeUpdateProfileDTO updateRequest = new TraineeUpdateProfileDTO();
        updateRequest.setUsername("john_doe");
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");
        updateRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updateRequest.setAddress("123 Main St");
        updateRequest.setActive(true);

        when(traineeService.updateTraineeProfile(any(TraineeUpdateProfileDTO.class)))
                .thenReturn(new TraineeProfileDTO("John", "Doe", "1990-01-01", "123 Main St", true, new ArrayList<>()));

        mockMvc.perform(put("/gym/trainee/profile-update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
                .andExpect(jsonPath("$.address").value("123 Main St"));
//                .andExpect(jsonPath("$.isActive").value(true));
    }


    @Test
    void deleteTraineeProfile_Success_Test() throws Exception {
        String username = "john_doe";

        doNothing().when(traineeService).deleteTraineeByUsername(username);

        mockMvc.perform(delete("/gym/trainee/profile-delete")
                        .param("username", username))
                .andExpect(status().isOk())  // 200
                .andExpect(content().string(""));
    }

    @Test
    void testGetNotAssignedActiveTrainers_Success() throws Exception {
        String username = "john_doe";

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setIsActive(true);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setIsActive(true);

        TrainingType specialization1 = new TrainingType();
        specialization1.setTrainingTypeName("Run");

        TrainingType specialization2 = new TrainingType();
        specialization2.setTrainingTypeName("Yoga");

        Trainer trainer1 = new Trainer();
        trainer1.setUser(user1);
        trainer1.setSpecialization(specialization1);

        Trainer trainer2 = new Trainer();
        trainer2.setUser(user2);
        trainer2.setSpecialization(specialization2);

        List<Trainer> trainers = Arrays.asList(trainer1, trainer2);
        when(trainerService.getAvailableTrainersForTrainee(username)).thenReturn(trainers);

        mockMvc.perform(get("/gym/trainee/unassigned-trainers")
                        .param("username", username)) // Параметр передається через .param
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$[0].user.username").value("user1"))
                .andExpect(jsonPath("$[1].user.username").value("user2"))
                .andExpect(jsonPath("$[0].specialization.trainingTypeName").value("Run"))
                .andExpect(jsonPath("$[1].specialization.trainingTypeName").value("Yoga"));
    }

    @Test
    void updateTraineeTrainers_Success_Test() throws Exception {
        String username = "john_doe";
        List<String> trainerUsernames = Arrays.asList("trainer1", "trainer2");

        UserDTO user1 = new UserDTO("John", "Doe", "trainer1", true);
        UserDTO user2 = new UserDTO("Jane", "Smith", "trainer2", true);

        TrainingType specialization1 = new TrainingType();
        specialization1.setTrainingTypeName("Yoga");

        TrainingType specialization2 = new TrainingType();
        specialization2.setTrainingTypeName("Pilates");

        TrainerDTO trainer1 = new TrainerDTO(user1, specialization1);
        TrainerDTO trainer2 = new TrainerDTO(user2, specialization2);

        List<TrainerDTO> updatedTrainers = Arrays.asList(trainer1, trainer2);

        when(traineeService.updateTraineeTrainers(username, trainerUsernames)).thenReturn(updatedTrainers);

        mockMvc.perform(put("/gym/trainee/trainers?username=" + username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerUsernames)))
                .andExpect(status().isOk())  // Перевірка статусу 200
                .andExpect(jsonPath("$[0].user.username").value("trainer1"))
                .andExpect(jsonPath("$[1].user.username").value("trainer2"))
                .andExpect(jsonPath("$[0].specialization.trainingTypeName").value("Yoga"))
                .andExpect(jsonPath("$[1].specialization.trainingTypeName").value("Pilates"));
    }

    @Test
    void updateTraineeStatus_Success_Test() throws Exception {
        String username = "john_doe";
        boolean isActive = true;

        doNothing().when(traineeService).switchTraineeActivation(username, isActive);

        mockMvc.perform(patch("/gym/trainee/status")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive)))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$").value("Trainee status updated successfully"));
    }
}
