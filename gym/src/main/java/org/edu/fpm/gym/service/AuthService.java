package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainee.TraineeDTO;
import org.edu.fpm.gym.dto.trainer.TrainerDTO;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.jwt.JwtUtil;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.UserRepository;
import org.edu.fpm.gym.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       UserService userService, TraineeRepository traineeRepository,
                       TrainerRepository trainerRepository, AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // pas: FwZtRXxCCg
    // username: John.Doe8
    public String authenticateUser(String username, String password) {
        try {
            userDetailsService.loadUserByUsername(username);

            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Invalid credentials");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            return jwtUtil.generateToken(authentication.getName());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("An error occurred during authentication", e);
        }
    }

    public Trainee createTrainee(@RequestBody TraineeDTO traineeDto) {
        var user = UserUtils.getUserInstance(
                traineeDto.user().firstName(),
                traineeDto.user().lastName(),
                userService.generateUsername(traineeDto.user().firstName(), traineeDto.user().lastName()),
                userService.generatePassword(), traineeDto.user().isActive());

        var trainee = new Trainee();
        trainee.setUser(user);
        trainee.setAddress(traineeDto.address());
        trainee.setDateOfBirth(traineeDto.dateOfBirth());
        user.setTrainees(trainee);
        traineeRepository.save(trainee);
        userService.createUser(user);

        log.info("Created trainee with username: {} and password: {}", traineeDto.user().username(), user.getPassword());
        return trainee;
    }

    public Trainer createTrainer(@RequestBody TrainerDTO trainerDTO) {
        Trainer createdTrainer;
        log.info("Creating trainer: {}", trainerDTO.user().firstName());
        try {

            User user = UserUtils.getUserInstance(
                    trainerDTO.user().firstName(),
                    trainerDTO.user().lastName(),
                    userService.generateUsername(trainerDTO.user().firstName(), trainerDTO.user().lastName()),
                    userService.generatePassword(), trainerDTO.user().isActive());

            var trainer = new Trainer();
            trainer.setUser(user);
            trainer.setSpecialization(trainerDTO.specialization());
            trainerRepository.save(trainer);
            userService.createUser(user);

            createdTrainer = trainerRepository.save(trainer);
        } catch (Exception e) {
            throw new RuntimeException("Error creating trainer");
        }

        log.info("Trainer created successfully: {}", createdTrainer.getUser().getUsername());
        return createdTrainer;
    }
}
