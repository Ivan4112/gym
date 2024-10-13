package org.edu.fpm.gym.dao;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.storage.TraineeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TraineeDao {
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
        return trainee;
    }

    public Trainee update(Trainee trainee) {
        Map<Long, Trainee> trainees = traineeStorage.getTrainees();
        Long id = trainee.getUserId();
        if(trainees.containsKey(id)) {
            trainees.put(id, trainee);
            return trainee;
        }else {
            throw new IllegalArgumentException("Trainee with ID " + id + " not found.");
        }
    }

    public void delete(Long userId) {
        Map<Long, Trainee> trainees = traineeStorage.getTrainees();
        if(trainees.containsKey(userId)) {
            trainees.remove(userId);
        }else {
            throw new IllegalArgumentException("Trainee with ID " + userId + " not found.");
        }
    }

    public Trainee findById(Long userId) {
        return traineeStorage.getTrainees().get(userId);
    }

    public String findAll() {
        return traineeStorage.getTrainees().values()
                .stream().map(Trainee::toString).collect(Collectors.joining("\n"));
    }

    private Long generateNewId() {
        return traineeStorage.getTrainees().keySet().stream()
                .max(Long::compare)
                .orElse(0L) + 1;
    }
}
