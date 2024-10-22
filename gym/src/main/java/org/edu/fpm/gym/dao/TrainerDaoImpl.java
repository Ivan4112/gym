package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TrainerDaoImpl implements TrainerRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public Trainer save(Trainer trainer) {
        try {
            log.info("Saving trainer: {}", trainer.getUser().getUsername());
            entityManager.persist(trainer);
            log.info("Successfully saved trainer: {}", trainer.getUser().getUsername());
            return trainer;
        } catch (Exception e) {
            log.error("Failed to save trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Trainer findByUsername(String username) {
        try {
            log.info("Finding trainer by username: {}", username);
            Trainer trainer = entityManager.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                    .setParameter("username", username)
                    .getSingleResult();
            log.info("Successfully found trainer: {}", username);
            return trainer;
        } catch (NoResultException e) {
            log.warn("No trainer found for username: {}", username);
            return null;
        } catch (Exception e) {
            log.error("Failed to find trainer by username: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        try {
            log.info("Updating trainer: {}", trainer.getUser().getUsername());
            entityManager.merge(trainer);
            log.info("Successfully updated trainer: {}", trainer.getUser().getUsername());
        } catch (Exception e) {
            log.error("Failed to update trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void changePassword(String username, String newPassword) {
        try {
            log.info("Changing password for trainer: {}", username);
            Trainer trainer = findByUsername(username);
            if (trainer != null) {
                trainer.getUser().setPassword(newPassword);
                updateTrainer(trainer);
                log.info("Password changed successfully for trainer: {}", username);
            } else {
                log.warn("Trainer not found for username: {}", username);
            }
        } catch (Exception e) {
            log.error("Failed to change password for trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void switchActivation(String username) {
        try {
            log.info("Switching activation for trainer: {}", username);
            Trainer trainer = findByUsername(username);
            if (trainer != null) {
                trainer.getUser().setIsActive(!trainer.getUser().getIsActive());
                updateTrainer(trainer);
                log.info("Successfully switched activation for trainer: {}", username);
            } else {
                log.warn("Trainer not found: {}", username);
            }
        } catch (Exception e) {
            log.error("Failed to switch activation for trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteByUsername(String username) {
        try {
            log.info("Deleting trainer by username: {}", username);
            Trainer trainer = findByUsername(username);
            if (trainer != null) {
                entityManager.remove(trainer);
                log.info("Successfully deleted trainer: {}", username);
            } else {
                log.warn("Can't delete, trainer not found: {}", username);
            }
        } catch (Exception e) {
            log.error("Failed to delete trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Trainer> findAvailableTrainersForTrainee(String traineeUsername) {
        try {
            log.info("Finding available trainers for trainee: {}", traineeUsername);

            List<Trainer> trainers = entityManager.createQuery(
                    "SELECT t FROM Trainer t WHERE t.id NOT IN " +
                            "(SELECT tr.id FROM Trainee ta JOIN ta.trainers tr WHERE ta.user.username = :username)", Trainer.class)
                    .setParameter("username", traineeUsername)
                    .getResultList();

            log.info("Successfully found available trainers for trainee: {}", traineeUsername);
            return trainers;
        } catch (Exception e) {
            log.error("Failed to find available trainers for trainee: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        try {
            log.info("Getting trainings for trainer: {} from {} to {}", username, fromDate, toDate);

            List<Training> trainings = entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainer.user.username = :username " +
                            "AND tr.trainingDate >= :fromDate AND tr.trainingDate <= :toDate " +
                            (traineeName != null ? "AND tr.trainee.user.username = :traineeName" : ""), Training.class)
                    .setParameter("username", username)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .setParameter("traineeName", traineeName)
                    .getResultList();

            log.info("Successfully retrieved trainings for trainer: {}", username);
            return trainings;
        } catch (Exception e) {
            log.error("Failed to get trainings for trainer: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        try {
            log.info("Finding trainer by first name: {} and last name: {}", firstName, lastName);

            String jpql = "SELECT tr FROM Trainer tr WHERE tr.user.firstName = :firstName AND tr.user.lastName = :lastName";
            TypedQuery<Trainer> query = entityManager.createQuery(jpql, Trainer.class);
            query.setParameter("firstName", firstName);
            query.setParameter("lastName", lastName);
            List<Trainer> results = query.getResultList();

            if (results.isEmpty()) {
                log.warn("No trainer found for first name: {} and last name: {}", firstName, lastName);
                return Optional.empty();
            } else {
                log.info("Successfully found trainer by first name: {} and last name: {}", firstName, lastName);
                return Optional.of(results.get(0));
            }
        } catch (Exception e) {
            log.error("Failed to find trainer by first name: {} and last name: {}: {}", firstName, lastName, e.getMessage(), e);
            throw e;
        }
    }
}
