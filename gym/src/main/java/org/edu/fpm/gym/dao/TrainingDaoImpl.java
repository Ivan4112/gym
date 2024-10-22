package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class TrainingDaoImpl implements TrainingRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Training save(Training training) {
        try {
            log.info("Saving training: {}", training);
            entityManager.persist(training);
            log.info("Successfully saved training: {}", training);
            return training;
        } catch (Exception e) {
            log.error("Failed to save training: {}. Error: {}", training, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Training> findByTraineeId(Long traineeId) {
        try {
            log.info("Finding trainings for traineeId: {}", traineeId);
            List<Training> trainings = entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainee.id = :traineeId", Training.class)
                    .setParameter("traineeId", traineeId)
                    .getResultList();
            log.info("Successfully found {} trainings for traineeId: {}", trainings.size(), traineeId);
            return trainings;
        } catch (Exception e) {
            log.error("Failed to find trainings for traineeId: {}. Error: {}", traineeId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Training> findByTrainerId(Long trainerId) {
        try {
            log.info("Finding trainings for trainerId: {}", trainerId);
            List<Training> trainings = entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainer.id = :trainerId", Training.class)
                    .setParameter("trainerId", trainerId)
                    .getResultList();
            log.info("Successfully found {} trainings for trainerId: {}", trainings.size(), trainerId);
            return trainings;
        } catch (Exception e) {
            log.error("Failed to find trainings for trainerId: {}. Error: {}", trainerId, e.getMessage(), e);
            throw e;
        }
    }
}
