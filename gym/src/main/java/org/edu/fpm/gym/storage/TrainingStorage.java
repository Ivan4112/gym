package org.edu.fpm.gym.storage;

import lombok.Getter;
import lombok.Setter;
import org.edu.fpm.gym.entity.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Component
public class TrainingStorage {
    private Map<Long, Training> trainings = new HashMap<>();

}
