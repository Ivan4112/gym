package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public TrainingType save(TrainingType trainingType) {
        entityManager.persist(trainingType);
        return trainingType;
    }

    @Override
    public TrainingType findTrainingTypeByName(String trainingTypeName) {
        return entityManager.createQuery("SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :trainingTypeName", TrainingType.class)
                .setParameter("trainingTypeName", trainingTypeName)
                .getSingleResult();
    }
}
