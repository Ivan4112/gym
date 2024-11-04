package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.AddTrainingDTO;
import org.edu.fpm.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@OpenAPIDefinition(
        info = @Info(title = "Training Management", version = "v1")
)
@RestController
@RequestMapping("/gym/training")
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
    // 14. Add Training (POST method)
    @Operation(summary = "Add training", description = "Add training")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training was successfully added"),
            @ApiResponse(responseCode = "400", description = "Invalid activation parameter")
    })
    @PostMapping("/add")
    public ResponseEntity<String> addTraining(@RequestBody AddTrainingDTO request) {
        boolean success = trainingService.addTraining(
                request.getTraineeUsername(),
                request.getTrainerUsername(),
                request.getTrainingName(),
                request.getTrainingDate(),
                request.getTrainingDuration()

        );

        if (success) {
            return ResponseEntity.ok("Training added successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to add training");
        }
    }
}
