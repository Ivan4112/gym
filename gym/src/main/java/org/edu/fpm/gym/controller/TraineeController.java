package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeProfileDTO;
import org.edu.fpm.gym.dto.trainee.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINEE;

@RestController
@RequestMapping(TRAINEE)
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TraineeController {
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @Autowired
    public TraineeController(TraineeService traineeService,
                             TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @Operation(summary = "Get trainee profile")
    @GetMapping("/profile")
    public TraineeProfileDTO getTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        return traineeService.getTraineeProfile(username);
    }

    @Operation(summary = "Update trainee profile")
    @PutMapping("/profile-update")
    public TraineeProfileDTO updateTraineeProfile(@Valid @RequestBody TraineeUpdateProfileDTO request) {
        return traineeService.updateTraineeProfile(request);
    }

    @Operation(summary = "Delete trainee profile")
    @DeleteMapping("/profile-delete")
    public String deleteTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        traineeService.deleteTraineeByUsername(username);
        return "Successfully deleted";
    }

    @Operation(summary = "Get not assigned active trainers")
    @GetMapping("/unassigned-trainers")
    public List<Trainer> getNotAssignedActiveTrainers(@NotEmpty @RequestParam("username") String username) {
        return trainerService.getAvailableTrainersForTrainee(username);
    }

    @Operation(summary = "Update trainee's trainer list")
    @PutMapping("/trainers")
    public List<TrainerDTO> updateTraineeTrainers(@NotEmpty @RequestParam("username") String username,
                                                  @Valid @RequestBody List<String> trainerUsernames) {
        return traineeService.updateTraineeTrainers(username, trainerUsernames);
    }

    @Operation(summary = "Get trainee trainings list")
    @GetMapping("/trainings")
    public List<TrainingDTO> getTraineeTrainingsList(TrainingRequestDTO trainingRequestDTO) {
        return traineeService.getTraineeTrainings(trainingRequestDTO);
    }

    @Operation(summary = "Activate/De-Activate Trainee",
            description = "Updates the active status of a trainee")
    @PatchMapping("/status")
    public String updateTraineeStatus(
            @NotEmpty @RequestParam("username") String username,
            @NotNull @RequestParam("isActive") boolean isActive) {
        traineeService.switchTraineeActivation(username, isActive);
        return "Trainee status updated successfully";
    }
}
