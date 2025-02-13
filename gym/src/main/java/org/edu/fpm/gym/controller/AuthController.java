package org.edu.fpm.gym.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.edu.fpm.gym.utils.ApiPaths.AUTH;

@RestController
@RequestMapping(AUTH)
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login to the system")
    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        log.info("Attempting login with username: {}", username);

        String token = authService.authenticateUser(username, password);
        return ResponseEntity.ok(token);
    }

    // pas: FwZtRXxCCg
    // username: John.Doe8
    @Operation(summary = "Register a new trainee")
    @PostMapping("/register/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    public Trainee registerTrainee(@RequestBody TraineeDTO traineeDto) {
        return authService.createTrainee(traineeDto);
    }

//    login: test.lastName7
//    pass: UJeZDn75Kx
    @Operation(summary = "Register a new trainer")
    @PostMapping("/register/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public Trainer registerTrainer(@RequestBody TrainerDTO trainerDTO) {
        return authService.createTrainer(trainerDTO);
    }
}
