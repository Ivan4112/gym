package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TrainerDao {
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
        return trainer;
    }

    public Trainer findById(Long id) {
        return trainerStorage.getTrainers().get(id);
    }

    public Trainer update(Trainer trainer) {
        Map<Long, Trainer> trainers = trainerStorage.getTrainers();
        Long id = trainer.getUserId();
        if (trainers.containsKey(id)) {
            trainers.put(id, trainer);
            return trainer;
        } else {
            throw new IllegalArgumentException("Trainer with ID " + id + " not found.");
        }
    }

    public String findAll() {
        return trainerStorage.getTrainers().values()
                .stream().map(Trainer::toString).collect(Collectors.joining("\n"));
    }

    private Long generateNewId() {
        return trainerStorage.getTrainers().keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1;
    }


}
