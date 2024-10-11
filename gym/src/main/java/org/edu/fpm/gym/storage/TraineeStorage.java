package org.edu.fpm.gym.storage;

import lombok.Getter;
import lombok.Setter;
import org.edu.fpm.gym.entity.Trainee;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Component
public class TraineeStorage {
    private Map<Long, Trainee> trainees = new HashMap<>();

}
