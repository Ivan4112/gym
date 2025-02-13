package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.edu.fpm.gym.dto.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINER;

@RestController
@RequestMapping(TRAINER)
@PreAuthorize("isAuthenticated()")
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(summary = "Get trainer profile")
    @GetMapping("/profile")
    public TrainerProfileDTO getTrainerProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        return trainerService.getTrainerProfile(username);
    }

    @GetMapping("/monthly-summary")
    public TrainerWorkloadSummaryDTO getMonthlyWorkload(@Valid @NotEmpty @RequestParam("username") String username) {
        return trainerService.getTrainerMonthlyWorkload(username);
    }

    @Operation(summary = "Update trainer profile")
    @PutMapping("/profile-update")
    public TrainerProfileDTO updateTrainerProfile(@RequestBody TrainerUpdateProfileDTO request) {
        return trainerService.updateTrainerProfile(request);
    }

    @Operation(summary = "Get list of trainer's trainings")
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainerTrainings(TrainingRequestDTO trainingRequestDTO) {
        return trainerService.getTrainingsForTrainer(trainingRequestDTO);
    }

    @Operation(summary = "Activate/Deactivate trainer")
    @PatchMapping("/status")
    public String switchTrainerActivation(@Valid @NotEmpty @RequestParam(name = "username") String username,
                                          @RequestParam("isActive") boolean isActive) {
        trainerService.switchTrainerActivation(username, isActive);
        return "Trainer status updated successfully";
    }
}
