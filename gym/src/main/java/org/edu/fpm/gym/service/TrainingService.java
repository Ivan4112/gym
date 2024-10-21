package org.edu.fpm.gym.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;

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
