package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TrainingDao;
import org.edu.fpm.gym.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private TrainingDao trainingDao;

    @Autowired
    public void setTrainingDao(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    public Training createTraining(Training training) {
        return trainingDao.save(training);
    }

    public Training getTrainingById(Long id) {
        return trainingDao.findById(id);
    }

    public String getAllTrainings() {
        logger.info("Retrieving all trainings");
        return trainingDao.findAll();
    }
}
