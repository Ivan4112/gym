package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    TrainingType findTrainingTypeByTrainingTypeName(String trainingTypeName);
}
