package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.edu.fpm.gym.dto.TrainingTypeDTO;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@OpenAPIDefinition(
        info = @Info(title = "TrainingType Management", version = "v1")
)
@RestController
@RequestMapping("/gym/trainingType")
public class TrainingTypeController {

    private final TrainingTypeService trainingTypeService;

    @Autowired
    public TrainingTypeController(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }
    //  17. Get Training types (GET method)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TrainingType was successfully received"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/get")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeService.findAllTrainingTypes();

        List<TrainingTypeDTO> response = trainingTypes.stream()
                .map(type -> new TrainingTypeDTO(type.getId(), type.getTrainingTypeName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
