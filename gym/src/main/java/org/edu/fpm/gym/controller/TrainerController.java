package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.edu.fpm.gym.dto.TrainerDTO;
import org.edu.fpm.gym.dto.TrainerProfileDTO;
import org.edu.fpm.gym.dto.TrainerUpdateProfileDTO;
import org.edu.fpm.gym.dto.TrainingDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/gym/trainer")
@OpenAPIDefinition(
        info = @Info(title = "Trainer Management", version = "v1")
)
public class TrainerController {

    private final TrainerService trainerService;
    
    private final UserService userService;

    @Autowired
    public TrainerController(TrainerService trainerService, UserService userService) {
        this.trainerService = trainerService;
        this.userService = userService;
    }

    // 2. Trainer Registration (POST method)
    @Operation(summary = "Register a new trainer", description = "Registers a new trainer along with the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered trainer"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error occurred during trainer registration")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerTrainer(@Valid @RequestBody TrainerDTO trainerDTO) {
        User user = new User();
        user.setFirstName(trainerDTO.getUser().getFirstName());
        user.setLastName(trainerDTO.getUser().getLastName());
        user.setPassword(userService.generatePassword());
        user.setUsername(userService.generateUsername(trainerDTO.getUser().getFirstName(), trainerDTO.getUser().getLastName()));
        user.setIsActive(true);

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(trainerDTO.getSpecialization());
        trainerService.createTrainer(trainer);
        userService.createUser(user);

        return ResponseEntity.ok("ok");
    }

    // 8. Get Trainer Profile (GET method)
    @Operation(summary = "Get trainer profile", description = "Fetches the profile of a trainer by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainer profile"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid username parameter")
    })
    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileDTO> getTrainerProfile(@Valid @NotEmpty @RequestParam("username") String username) {
        TrainerProfileDTO profile = trainerService.getTrainerProfile(username);
        return ResponseEntity.ok(profile);
    }

    // 9. Update Trainer Profile (PUT method)
    @Operation(summary = "Update trainer profile", description = "Updates the profile details of a trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid profile update data"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PutMapping("/profile-update")
    public ResponseEntity<TrainerProfileDTO> updateTrainerProfile(@RequestBody TrainerUpdateProfileDTO request) {
        TrainerProfileDTO updatedProfile = trainerService.updateTrainerProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }

    // 13. Get Trainer Trainings List (GET method)
    @Operation(summary = "Get list of trainer's trainings", description = "Fetches the list of trainings for a specific trainer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trainer trainings"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer or trainings not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
   @GetMapping("/trainings")
    public ResponseEntity<List<TrainingDTO>> getTrainerTrainings(@Valid @NotEmpty @RequestParam(name = "username") String username,
                                                                  @RequestParam(name = "fromDate", required = false) LocalDate fromDate,
                                                                  @RequestParam(name = "toDate", required = false) LocalDate toDate,
                                                                  @RequestParam(name = "trainerName", required = false) String trainerName) {
        List<TrainingDTO> trainings = trainerService.getTrainingsForTrainer(username, fromDate, toDate, trainerName);
        return ResponseEntity.ok(trainings);
    }

    // 16. Activate/De-Activate Trainer (PATCH method)
    @Operation(summary = "Activate/Deactivate trainer", description = "Activate or deactivate a trainer based on the provided status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
            @ApiResponse(responseCode = "401", description = "User unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainer not found"),
            @ApiResponse(responseCode = "400", description = "Invalid activation parameter")
    })
    @PatchMapping("/status")
    public ResponseEntity<String> switchTrainerActivation(@Valid @NotEmpty @RequestParam(name = "username") String username, @RequestParam("isActive") boolean isActive) {
        trainerService.switchTrainerActivation(username, isActive);
        return ResponseEntity.ok("Trainer status updated successfully");
    }
}
