package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TraineeDao;
import org.edu.fpm.gym.entity.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
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
        logger.info("Retrieving all trainees");
        return traineeDao.findAll();
    }
}
