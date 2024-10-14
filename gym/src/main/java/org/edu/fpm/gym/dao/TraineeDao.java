package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.storage.TraineeStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TraineeDao {
    private static final Logger logger = LoggerFactory.getLogger(TraineeDao.class);
    TraineeStorage traineeStorage;

    @Autowired
    public void setStorage(TraineeStorage traineeStorage) {
        this.traineeStorage = traineeStorage;
    }

    public Trainee save(Trainee trainee) {
        Map<Long, Trainee> trainees = traineeStorage.getTrainees();
        Long newId = generateNewId();
        trainee.setUserId(newId);
        trainees.put(newId, trainee);
        logger.info("Saved trainee with ID: {}", trainee.getUserId());
        return trainee;
    }

    public Trainee update(Trainee trainee) {
        Map<Long, Trainee> trainees = traineeStorage.getTrainees();
        Long id = trainee.getUserId();
        if(trainees.containsKey(id)) {
            trainees.put(id, trainee);
            logger.info("Updated trainee with ID {}", id);
            return trainee;
        }else {
            String errorMessage = "Trainee with ID " + id + " not found.";
            logger.warn(errorMessage);
            throw new IllegalArgumentException();
        }
    }

    public void delete(Long userId) {
        Map<Long, Trainee> trainees = traineeStorage.getTrainees();
        if(trainees.containsKey(userId)) {
            logger.info("Removed trainee with ID {}", userId);
            trainees.remove(userId);
        }else {
            String errorMessage = "Can't remove trainee with ID " + userId;
            logger.warn(errorMessage);
            throw new IllegalArgumentException();
        }
    }

    public Trainee findById(Long userId) {
        Trainee trainee = traineeStorage.getTrainees().get(userId);
        if (trainee != null) {
            logger.info("Found trainee with ID {}", userId);
        } else {
            logger.warn("Trainee with ID {} not found", userId);
        }
        return trainee;
    }

    public String findAll() {
        return traineeStorage.getTrainees().values()
                .stream().map(Trainee::toString).collect(Collectors.joining("\n"));
    }

    public Long generateNewId() {
        return traineeStorage.getTrainees().keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1;
    }
}
