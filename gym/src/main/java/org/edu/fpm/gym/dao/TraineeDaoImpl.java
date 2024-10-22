package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Trainee save(Trainee trainee) {
        entityManager.persist(trainee);
        return trainee;
    }

    @Override
    public Trainee findByUsername(String username) {
        try {
            Trainee trainee = entityManager.createQuery("SELECT t FROM Trainee t JOIN FETCH t.user WHERE t.user.username = :username", Trainee.class)
                    .setParameter("username", username)
                    .getSingleResult();
            log.info("Successfully found trainee by username: {}", username);
            return trainee;
        } catch (NoResultException e) {
            log.warn("No trainee found with username: {}", username);
            return null;
        } catch (Exception e) {
            log.error("Error finding trainee by username: {}", username, e);
            throw e;
        }
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        try {
            log.info("Updating trainee: {}", trainee.getUser().getUsername());
            entityManager.merge(trainee);
            log.info("Successfully updated trainee: {}", trainee.getUser().getUsername());
        } catch (Exception e) {
            log.error("Failed to update trainee: {}. Error: {}", trainee.getUser().getUsername(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void changePassword(String username, String newPassword) {
        try {
            Trainee trainee = findByUsername(username);
            if (trainee == null) {
                log.warn("No found trainee with username: {}", username);
                return;
            }
            trainee.getUser().setPassword(newPassword);
            updateTrainee(trainee);
            log.info("Password changed for trainee: {}", username);
        } catch (Exception e) {
            log.error("Failed to change password for trainee: {}. Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void switchActivation(String username) {
        try {
            Trainee trainee = findByUsername(username);
            if (trainee == null) {
                log.warn("Trainee not found with username: {}", username);
            } else {
                trainee.getUser().setIsActive(!trainee.getUser().getIsActive());
                updateTrainee(trainee);
                log.info("Trainee activation switched for username: {}", username);
            }
        } catch (Exception e) {
            log.error("Failed to switch activation for trainee: {}. Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try {
            Trainee trainee = findByUsername(username);
            if (trainee == null) {
                log.warn("Not found trainee for deleting: {}", username);
            } else {
                entityManager.remove(trainee);
                log.info("Successfully deleted trainee: {}", username);
            }
        } catch (Exception e) {
            log.error("Failed to delete trainee: {}. Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        try {
            log.info("Fetching trainings for trainee: {} from {} to {}", username, fromDate, toDate);
            List<Training> trainings = entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainee.user.username = :username " +
                            "AND tr.trainingDate >= :fromDate AND tr.trainingDate <= :toDate " +
                            (trainerName != null ? "AND tr.trainer.user.username = :trainerName" : "") +
                            (trainingType != null ? "AND tr.trainingType = :trainingType" : ""), Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("trainerName", trainerName)
                    .setParameter("trainingType", trainingType)
                    .getResultList();
            log.info("Successfully fetched trainings for trainee: {}", username);
            return trainings;
        } catch (Exception e) {
            log.error("Failed to fetch trainings for trainee: {}. Error: {}", username, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<Trainee> findByFirstNameAndLastName(String firstName, String lastName) {
        try {
            log.info("Finding trainee by first name: {} and last name: {}", firstName, lastName);
            String jpql = "SELECT t FROM Trainee t WHERE t.user.firstName = :firstName AND t.user.lastName = :lastName";
            TypedQuery<Trainee> query = entityManager.createQuery(jpql, Trainee.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            List<Trainee> results = query.getResultList();
            log.info("Successfully found trainee by name: {} {}", firstName, lastName);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            log.error("Failed to find trainee by name: {} {}. Error: {}", firstName, lastName, e.getMessage(), e);
            throw e;
        }
    }
}
