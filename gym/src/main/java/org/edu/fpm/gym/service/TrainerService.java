package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TrainerDao;
import org.edu.fpm.gym.entity.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    private TrainerDao trainerDao;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer createTrainer(Trainer trainer) {
        return trainerDao.save(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerDao.update(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerDao.findById(id);
    }

    public String getAllTrainers() {
        return trainerDao.findAll();
    }
}
