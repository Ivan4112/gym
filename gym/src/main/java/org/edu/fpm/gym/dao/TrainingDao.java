package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.storage.TrainingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;

@Repository
public class TrainingDao {
    TrainingStorage trainingStorage;

    @Autowired
    public void setTrainingStorage(TrainingStorage trainingStorage) {
        this.trainingStorage = trainingStorage;
    }

    public Training save(Training training) {
        Map<Long, Training> trainings = trainingStorage.getTrainings();
        Long newId = generateUniqueKey(training);
        trainings.put(newId, training);
        return training;
    }

    public Training findById(Long id) {
        return trainingStorage.getTrainings().get(id);
    }

    private Long generateUniqueKey(Training training) {
        return Objects.hash(training.getTraineeId(), training.getTrainerId()) & 0xFFFFFFFFL;
    }
}
