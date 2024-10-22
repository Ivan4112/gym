package org.edu.fpm.gym;

import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingTypeService;
import org.edu.fpm.gym.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
@ComponentScan(basePackages = "org.edu.fpm")
@Slf4j
public class GymApplication {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(GymApplication.class);
        TraineeService traineeService = context.getBean(TraineeService.class);
        UserService userService = context.getBean(UserService.class);
        TrainingTypeService trainingTypeService = context.getBean(TrainingTypeService.class);
        TrainerService trainerService = context.getBean(TrainerService.class);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("App");
        user.setUsername("john.app");
        user.setPassword("securePassword123");
        user.setIsActive(true);
        userService.createUser(user);
        log.info("Created user profile:{}", user.getUsername());

        TrainingType specialization = trainingTypeService.findTrainingTypeByName("Workout");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        trainer.setTrainees(new HashSet<>());
        trainerService.createTrainer(trainer);

        log.info("Created trainer:{}", trainer.getUser().getUsername());
    }

}
