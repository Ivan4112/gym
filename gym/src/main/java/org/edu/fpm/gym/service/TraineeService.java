package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TraineeDao;
import org.edu.fpm.gym.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeService {
    private TraineeDao traineeDao;

    @Autowired
    public void setTraineeDao(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeDao.save(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeDao.update(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeDao.delete(id);
    }

    public Trainee getTraineeById(Long id) {
        return traineeDao.findById(id);
    }

    public String getAllTrainees() {
        return traineeDao.findAll();
    }
}
