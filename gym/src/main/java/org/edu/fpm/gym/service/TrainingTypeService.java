package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class TrainingTypeService {
    final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingTypeService(TrainingTypeRepository trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public TrainingType createTrainingType(TrainingType trainingType) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Creating training type: {}", trainingType);
        TrainingType createdTrainingType = trainingTypeRepository.save(trainingType);
        MDC.clear();
        return createdTrainingType;
    }

    public TrainingType findTrainingTypeByName(String name) {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Finding training type by name: {}", name);
        TrainingType trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(name);

        MDC.clear();
        return trainingType;
    }

    public List<TrainingType> findAllTrainingTypes() {
        String transactionId = generateTransactionId();
        MDC.put("transactionId", transactionId);
        log.info("Fetching all training types, transactionId: {}", transactionId);
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        MDC.clear();

        return trainingTypes;
    }
}
