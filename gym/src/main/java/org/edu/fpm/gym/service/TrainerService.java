package org.edu.fpm.gym.service;

import org.edu.fpm.gym.dao.TrainerDao;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.security.SecurityCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private TrainerDao trainerDao;

    @Autowired
    SecurityCredential securityCredential;

    @Autowired
    public void setTrainerDao(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer createTrainee(String firstName, String lastName, String specialization) {
        Long newId = trainerDao.generateNewId();

        String username = securityCredential.generateUsername(firstName, lastName, newId);
        String password = securityCredential.generatePassword();

        Trainer newTrainee = new Trainer(newId, firstName, lastName, username, password, true, specialization);
        return trainerDao.save(newTrainee);
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
        logger.info("Retrieving all trainers");
        return trainerDao.findAll();
    }
}
