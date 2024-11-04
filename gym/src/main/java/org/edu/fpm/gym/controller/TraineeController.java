package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.*;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.exception.UnauthorizedException;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gym/trainee")
@Slf4j
@OpenAPIDefinition(
        info = @Info(title = "Trainee Management", version = "v1")
)
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;

    @Autowired
    public TraineeController(TraineeService traineeService,
                             TrainerService trainerService,
                             UserService userService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.userService = userService;
    }


    // 1. Trainee Registration (POST method)
    @Operation(summary = "Register a new trainee", description = "Registers a new trainee in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered trainee"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error occurred")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerTrainee(@Valid @RequestBody TraineeDTO traineeDto) {
        User user = new User();
        user.setFirstName(traineeDto.getUser().getFirstName());
        user.setLastName(traineeDto.getUser().getLastName());
        user.setPassword(userService.generatePassword());
        user.setUsername(userService.generateUsername(traineeDto.getUser().getFirstName(), traineeDto.getUser().getLastName()));
        user.setIsActive(true);


        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        user.setTrainees(trainee);
        traineeService.createTrainee(trainee);
        userService.createUser(user);


        return ResponseEntity.ok("ok");
    }

    // 5. Get Trainee Profile (GET method)
    @Operation(summary = "Get trainee profile", description = "Fetches the profile of a trainee based on username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainee profile"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/profile")
    public ResponseEntity<?> getTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        try {
            TraineeProfileDTO traineeProfile = traineeService.getTraineeProfile(username);
            return ResponseEntity.ok(traineeProfile);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("User unauthorized"));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Trainee not found"));
        }
    }

    // 6. Update Trainee Profile (PUT method)
    @Operation(summary = "Update trainee profile", description = "Updates the profile information of a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update request"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PutMapping("/profile-update")
    public ResponseEntity<TraineeProfileDTO> updateTraineeProfile(@Valid @RequestBody TraineeUpdateProfileDTO request) {
        TraineeProfileDTO updatedProfile = traineeService.updateTraineeProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }

    // 7. Delete Trainee Profile (DELETE method)
    @Operation(summary = "Delete trainee profile", description = "Deletes the profile of a trainee based on username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted trainee profile"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping("/profile-delete")
    public ResponseEntity<Void> deleteTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return ResponseEntity.ok().build();
    }

    // 10. Get not assigned active trainers
    @Operation(summary = "Get not assigned active trainers", description = "Fetches a list of active trainers who are not assigned to any trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully got active trainers"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainers not found")
    })
    @GetMapping("/unassigned-trainers")
    public ResponseEntity<List<Trainer>> getNotAssignedActiveTrainers(@NotEmpty @RequestParam("username") String username) {
        List<Trainer> trainers = trainerService.getAvailableTrainersForTrainee(username);
        return ResponseEntity.ok(trainers);
    }

    // 11. Update Trainee's Trainer List
    @Operation(summary = "Update trainee's trainer list", description = "Updates the list of trainers assigned to a specific trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated trainee"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PutMapping("/trainers")
    public ResponseEntity<List<TrainerDTO>> updateTraineeTrainers(@NotEmpty @RequestParam("username") String username,
                                                                  @NotEmpty @RequestBody List<String> trainerUsernames) {
        List<TrainerDTO> updatedTrainers = traineeService.updateTraineeTrainers(username, trainerUsernames);
        return ResponseEntity.ok(updatedTrainers);
    }

    // 12. Get Trainee Trainings List
    @Operation(summary = "Get trainee trainings list", description = "Fetches a list of trainings for a specific trainee, with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received trainings list for trainee"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "List not found")
    })
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingDTO>> getTraineeTrainingsList(
            @Valid @NotEmpty @RequestParam("username") String username,
            @Valid @RequestParam(name = "periodFrom", required = false) LocalDate periodFrom,
            @Valid @RequestParam(name = "periodTo", required = false) LocalDate periodTo,
            @Valid @RequestParam(name = "trainerName", required = false) String trainerName,
            @Valid @RequestParam(name = "trainingType",required = false) TrainingType trainingType) {
        List<TrainingDTO> trainings = traineeService.getTraineeTrainings(username, periodFrom, periodTo, trainerName, trainingType);
        return ResponseEntity.ok(trainings);
    }

    // 15. Activate/De-Activate Trainee
    @Operation(summary = "Activate/De-Activate Trainee", description = "Updates the active status of a trainee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated trainee's status"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PatchMapping("/status")
    public ResponseEntity<String> updateTraineeStatus(
            @NotEmpty @RequestParam("username") String username, @NotNull @RequestParam("isActive") boolean isActive) {
        traineeService.switchTraineeActivation(username, isActive);
        return ResponseEntity.ok("Trainee status updated successfully");
    }
}
