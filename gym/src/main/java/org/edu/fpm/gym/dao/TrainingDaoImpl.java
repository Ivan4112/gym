package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public Training save(Training training) {
        entityManager.persist(training);
        return training;
    }

    @Override
    public List<Training> findByTraineeId(Long traineeId) {
        return entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainee.id = :traineeId", Training.class)
                .setParameter("traineeId", traineeId)
                .getResultList();
    }

    @Override
    public List<Training> findByTrainerId(Long trainerId) {
        return entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainer.id = :trainerId", Training.class)
                .setParameter("trainerId", trainerId)
                .getResultList();
    }
}
