package org.edu.fpm.gym;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Menu {
    TraineeService traineeService;

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @PostConstruct
    public void runMenu() {
        log.info("Init program");
        log.info("Data has been initialized");
    }
}
