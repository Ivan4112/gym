package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TraineeDao;
import org.edu.fpm.gym.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeService {
    @Autowired
    private TraineeDao traineeDao;

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
}
