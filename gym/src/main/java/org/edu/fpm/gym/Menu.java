package org.edu.fpm.gym;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@Slf4j
public class Menu {
    private final GymFacade gymFacade;

    @Autowired
    public Menu(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @PostConstruct
    public void runMenu() {
        log.info("Init program");

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe");
        user.setPassword("securePassword123");
        user.setIsActive(true);
        gymFacade.createUser(user);

        TrainingType specialization = gymFacade.findTrainingTypeByName("Workout");

        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        trainer.setTrainees(new HashSet<>());
        gymFacade.createTrainerProfile(trainer);

        gymFacade.createTrainerProfile(trainer);
        log.info("Created trainer profile:{}", trainer.getUser().getUsername());

        log.info("Data has been initialized");
    }
}
