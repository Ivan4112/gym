package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.trainer.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINER;

@RestController
@RequestMapping(TRAINER)
public class TrainerController {

    private final TrainerService trainerService;

    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Operation(summary = "Register a new trainer")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Trainer registerTrainer(@Valid @RequestBody TrainerDTO trainerDTO) {
        return trainerService.createTrainer(trainerDTO);
    }

    @Operation(summary = "Get trainer profile")
    @GetMapping("/profile")
    public TrainerProfileDTO getTrainerProfile(@Valid @NotEmpty @RequestParam("username") String username,
                                               @Valid @NotEmpty @RequestParam("password") String password) {
        return trainerService.getTrainerProfile(username, password);
    }

    @Operation(summary = "Update trainer profile")
    @PutMapping("/profile-update")
    public TrainerProfileDTO updateTrainerProfile(@RequestBody TrainerUpdateProfileDTO request,
                                                  @NotEmpty @RequestParam("password") String password) {
        return trainerService.updateTrainerProfile(request, password);
    }

    @Operation(summary = "Get list of trainer's trainings")
    @GetMapping("/trainings")
    public List<TrainingDTO> getTrainerTrainings(TrainingRequestDTO trainingRequestDTO) {
        return trainerService.getTrainingsForTrainer(trainingRequestDTO);
    }

    @Operation(summary = "Activate/Deactivate trainer")
    @PatchMapping("/status")
    public String switchTrainerActivation(@Valid @NotEmpty @RequestParam(name = "username") String username,
                                          @RequestParam("isActive") boolean isActive,
                                          @Valid @NotEmpty @RequestParam("password") String password) {
        trainerService.switchTrainerActivation(username, isActive, password);
        return "Trainer status updated successfully";
    }
}
