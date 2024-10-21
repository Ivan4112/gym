package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.Training;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository {
    Training save(Training training);
    List<Training> findByTraineeId(Long traineeId);
    List<Training> findByTrainerId(Long trainerId);
}
