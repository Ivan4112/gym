package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TraineeDaoImpl implements TraineeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainee save(Trainee trainee) {
        entityManager.persist(trainee);
        return trainee;
    }

    @Override
    public Trainee findByUsername(String username) {
        return entityManager.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        entityManager.merge(trainee);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainee trainee = findByUsername(username);
        trainee.getUser().setPassword(newPassword);
        updateTrainee(trainee);
    }

    @Override
    public void switchActivation(String username) {
        Trainee trainee = findByUsername(username);
        trainee.getUser().setIsActive(!trainee.getUser().getIsActive());
        updateTrainee(trainee);
    }

    @Override
    public void deleteByUsername(String username) {
        Trainee trainee = findByUsername(username);
        entityManager.remove(trainee);
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        return entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainee.user.username = :username " +
                        "AND tr.trainingDate >= :fromDate AND tr.trainingDate <= :toDate " +
                        (trainerName != null ? "AND tr.trainer.user.username = :trainerName" : "") +
                        (trainingType != null ? "AND tr.trainingType = :trainingType" : ""), Training.class)
                .setParameter("username", username)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("trainerName", trainerName)
                .setParameter("trainingType", trainingType)
                .getResultList();
    }

    public Optional<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        String jpql = "SELECT t FROM Trainee t WHERE t.user.firstName = :firstName AND t.user.lastName = :lastName";
        TypedQuery<Trainee> query = entityManager.createQuery(jpql, Trainee.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        List<Trainee> results = query.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
