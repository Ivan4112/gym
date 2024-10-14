package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.storage.TrainingStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TrainingDao {
    private static final Logger logger = LoggerFactory.getLogger(TrainingDao.class);
    TrainingStorage trainingStorage;

    @Autowired
    public void setTrainingStorage(TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        Map<Long, Training> trainings = trainingStorage.getTrainings();
        Long newId = generateUniqueKey(training);
        trainings.put(newId, training);
        logger.info("Saved training with ID: {}", training);
        return training;
    }

    public Training findById(Long id) {
        Training training = trainingStorage.getTrainings().get(id);
        if(training != null) {
            logger.info("Found training with ID {}", id);
        } else {
            logger.warn("Training with ID {} not found", id);
        }
        return training;
    }

    public String findAll() {
        return trainingStorage.getTrainings().values()
                .stream().map(Training::toString).collect(Collectors.joining("\n"));
    }

    private Long generateUniqueKey(Training training) {
        return Objects.hash(training.getTraineeId(), training.getTrainerId()) & 0xFFFFFFFFL;
    }
}
