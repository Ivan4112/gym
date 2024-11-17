package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.trainingType.TrainingTypeDTO;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TrainingTypeService {
    final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public TrainingType createTrainingType(TrainingType trainingType) {
        log.info("Creating training type: {}", trainingType);
        return trainingTypeRepository.save(trainingType);
    }

    public TrainingType findTrainingTypeByName(String name) {
        log.info("Finding training type by name: {}", name);
        return trainingTypeRepository.findTrainingTypeByTrainingTypeName(name);
    }

    public List<TrainingTypeDTO> findAllTrainingTypes() {
        return trainingTypeRepository.findAll().stream()
                .map(type -> new TrainingTypeDTO(type.getId(), type.getTrainingTypeName())).toList();
    }
}
