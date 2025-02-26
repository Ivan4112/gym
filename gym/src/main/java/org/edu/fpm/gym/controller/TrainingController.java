package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINING;


@RestController
@RequestMapping(TRAINING)
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;
    private final TrainerService trainerService;

    @Autowired
    public TrainingController(TrainingService trainingService, TrainerService trainerService) {
        this.trainingService = trainingService;
        this.trainerService = trainerService;
    }

    @Operation(summary = "Add training")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTraining(@RequestBody AddTrainingDTO request) {
        return trainingService.addTraining(request);
    }

    @GetMapping("/find-trainings")
    public ResponseEntity<List<ExternalTrainingServiceDTO>> findTrainingByTrainerUsername(@RequestParam("username") String username) {
        Trainer trainer = trainerService.getTrainerByUsername(username);
        return trainingService.getTrainingsByTrainer(trainer.getId());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTraining(@RequestParam("id_training") Integer id) {
        try {
            trainingService.deleteTraining(id);
            return ResponseEntity.ok("Training deleted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Failed to delete training with ID {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete training.");
        }
    }
}
