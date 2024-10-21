package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.TrainingType;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository {
    TrainingType save(TrainingType trainingType);
    TrainingType findTrainingTypeByName(String trainingTypeName);
}
