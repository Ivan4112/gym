package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TrainingTypeDaoImpl implements TrainingTypeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public TrainingType save(TrainingType trainingType) {
        try {
            log.info("Saving training type: {}", trainingType);
            entityManager.persist(trainingType);
            log.info("Successfully saved training type: {}", trainingType);
            return trainingType;
        } catch (Exception e) {
            log.error("Failed to save training type: {}. Error: {}", trainingType, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TrainingType findTrainingTypeByName(String trainingTypeName) {
        try {
            log.info("Finding training type by name: {}", trainingTypeName);
            TrainingType trainingType = entityManager.createQuery("SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :trainingTypeName", TrainingType.class)
                    .setParameter("trainingTypeName", trainingTypeName)
                    .getSingleResult();
            log.info("Successfully found training type: {}", trainingType);
            return trainingType;
        } catch (NoResultException e) {
            log.warn("No training type found with name: {}", trainingTypeName);
            return null;
        } catch (Exception e) {
            log.error("Failed to find training type by name: {}. Error: {}", trainingTypeName, e.getMessage(), e);
            throw e;
        }
    }
}
