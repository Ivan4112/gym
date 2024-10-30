package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Training createTraining(Training training) {
        return trainingRepository.save(training);
    }

    public List<Training> getTrainingsByTrainee(Long traineeId) {
        return trainingRepository.findByTraineeId(traineeId);
    }

    public List<Training> getTrainingsByTrainer(Long trainerId) {
        return trainingRepository.findByTrainerId(trainerId);
    }
}
