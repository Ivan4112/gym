package org.edu.fpm.gym.controller;

import org.edu.fpm.gym.dto.trainingType.TrainingTypeDTO;
import org.edu.fpm.gym.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.edu.fpm.gym.utils.ApiPaths.TRAINING_TYPE;


@RestController
@RequestMapping(TRAINING_TYPE)
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    @GetMapping("/get")
    public List<TrainingTypeDTO> getTrainingTypes() {
        return trainingTypeService.findAllTrainingTypes();
    }
}
