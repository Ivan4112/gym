package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findByTraineeId(Integer trainee_id);
    List<Training> findByTrainerId(Integer trainer_id);
}
