package org.edu.fpm.gym.storage;

import lombok.Getter;
import lombok.Setter;
import org.edu.fpm.gym.entity.Trainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Component
public class TrainerStorage {
    private Map<Long, Trainer> trainers = new HashMap<>();

}
