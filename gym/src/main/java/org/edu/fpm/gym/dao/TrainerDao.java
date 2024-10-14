package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.storage.TrainerStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TrainerDao {
    private static final Logger logger = LoggerFactory.getLogger(TrainerDao.class);
    private TrainerStorage trainerStorage;

    @Autowired
    public void setTrainerStorage(TrainerStorage trainerStorage) {
        this.trainerStorage = trainerStorage;
    }

    public Trainer save(Trainer trainer) {
        Map<Long, Trainer> trainers = trainerStorage.getTrainers();
        Long newId = generateNewId();
        trainer.setUserId(newId);
        trainers.put(newId, trainer);
        logger.info("Saved trainer with ID: {}", trainer.getUserId());
        return trainer;
    }

    public Trainer findById(Long id) {
        Trainer trainer = trainerStorage.getTrainers().get(id);
        if (trainer != null) {
            logger.info("Found trainer with ID {}", id);
        } else {
            logger.warn("Trainer with ID {} not found", id);
        }
        return trainer;
    }

    public Trainer update(Trainer trainer) {
        Map<Long, Trainer> trainers = trainerStorage.getTrainers();
        Long id = trainer.getUserId();
        if (trainers.containsKey(id)) {
            trainers.put(id, trainer);
            logger.info("Updated trainer with ID {}", id);
            return trainer;
        } else {
            String errorMessage = "Trainer with ID " + id + " not found.";
            logger.warn(errorMessage);
            throw new IllegalArgumentException();
        }
    }

    public String findAll() {
        return trainerStorage.getTrainers().values()
                .stream().map(Trainer::toString).collect(Collectors.joining("\n"));
    }

    public Long generateNewId() {
        return trainerStorage.getTrainers().keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1;
    }


}
