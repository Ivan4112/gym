package org.edu.fpm.gym.service;

import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingTypeService {
    @Autowired
    TrainingTypeRepository trainingTypeRepository;

    public TrainingType createTrainingType(TrainingType trainingType) {
        return trainingTypeRepository.save(trainingType);
    }

    public TrainingType findTrainingTypeByName(String name) {
        return trainingTypeRepository.findTrainingTypeByName(name);
    }
}
