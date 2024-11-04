package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeDTO;
import org.edu.fpm.gym.dto.trainee.TraineeProfileDTO;
import org.edu.fpm.gym.dto.trainee.TraineeUpdateProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINEE;

@RestController
@RequestMapping(TRAINEE)
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


    @Operation(summary = "Register a new trainee")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Trainee registerTrainee(@Valid @RequestBody TraineeDTO traineeDto) {
        return traineeService.createTrainee(traineeDto);
    }

    @Operation(summary = "Get trainee profile")
    @GetMapping("/profile")
    public TraineeProfileDTO getTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username,
                                               @Valid @NotEmpty @RequestParam("password") String password) {
        return traineeService.getTraineeProfile(username, password);
    }

    @Operation(summary = "Update trainee profile")
    @PutMapping("/profile-update")
    public TraineeProfileDTO updateTraineeProfile(@Valid @RequestBody TraineeUpdateProfileDTO request,
                                                  @Valid @NotEmpty @RequestParam("password") String password) {
        return traineeService.updateTraineeProfile(request, password);
    }

    @Operation(summary = "Delete trainee profile")
    @DeleteMapping("/profile-delete")
    public String deleteTraineeProfile(@Valid @NotEmpty @RequestParam("username") String username,
                                       @Valid @NotEmpty @RequestParam("password") String password) {
        traineeService.deleteTraineeByUsername(username, password);
        return "Successfully deleted";
    }

    @Operation(summary = "Get not assigned active trainers")
    @GetMapping("/unassigned-trainers")
    public List<Trainer> getNotAssignedActiveTrainers(@NotEmpty @RequestParam("username") String username,
                                                      @NotEmpty @RequestParam("password") String password) {
        return trainerService.getAvailableTrainersForTrainee(username, password);
    }

    @Operation(summary = "Update trainee's trainer list")
    @PutMapping("/trainers")
    public List<TrainerDTO> updateTraineeTrainers(@NotEmpty @RequestParam("username") String username,
                                                  @Valid @RequestBody List<String> trainerUsernames,
                                                  @Valid @NotEmpty @RequestParam("password") String password) {
        return traineeService.updateTraineeTrainers(username, trainerUsernames, password);
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
            @NotNull @RequestParam("isActive") boolean isActive,
            @Valid @NotEmpty @RequestParam("password") String password) {
        traineeService.switchTraineeActivation(username, isActive, password);
        return "Trainee status updated successfully";
    }
}
