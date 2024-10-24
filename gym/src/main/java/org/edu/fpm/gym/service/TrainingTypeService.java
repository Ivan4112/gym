package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TrainingTypeService {
    final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public TrainingType createTrainingType(TrainingType trainingType) {
        return trainingTypeRepository.save(trainingType);
    }

    public TrainingType findTrainingTypeByName(String name) {
        return trainingTypeRepository.findTrainingTypeByTrainingTypeName(name);
    }
}
