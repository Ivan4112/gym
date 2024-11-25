package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINING;


@RestController
@RequestMapping(TRAINING)
@PreAuthorize("isAuthenticated()")
@Slf4j
public class TrainingController {
    private final TrainingService trainingService;

    @Autowired
    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Operation(summary = "Add training")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addTraining(@RequestBody AddTrainingDTO request) {
        return trainingService.addTraining(request);
    }
}
