package org.edu.fpm.gym.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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

    @Override
    public Trainer save(Trainer trainer) {
        entityManager.persist(trainer);
        return trainer;
    }

    @Override
    public Trainer findByUsername(String username) {
        return entityManager.createQuery("SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        entityManager.merge(trainer);
    }

    @Override
    public void changePassword(String username, String newPassword) {
        Trainer trainer = findByUsername(username);
        trainer.getUser().setPassword(newPassword);
        updateTrainer(trainer);
    }

    @Override
    public void switchActivation(String username) {
        Trainer trainer = findByUsername(username);
        trainer.getUser().setIsActive(!trainer.getUser().getIsActive());
        updateTrainer(trainer);
    }

    @Override
    public void deleteByUsername(String username) {
        Trainer trainer = findByUsername(username);
        entityManager.remove(trainer);
    }

    @Override
    public List<Trainer> findAvailableTrainersForTrainee(String traineeUsername) {
        return entityManager.createQuery("SELECT t FROM Trainer t WHERE t.id NOT IN " +
                        "(SELECT tr.id FROM Trainee ta JOIN ta.trainers tr WHERE ta.user.username = :username)", Trainer.class)
                .setParameter("username", traineeUsername)
                .getResultList();
    }

    @Override
    public List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName) {
        return entityManager.createQuery("SELECT tr FROM Training tr WHERE tr.trainer.user.username = :username " +
                        "AND tr.trainingDate >= :fromDate AND tr.trainingDate <= :toDate " +
                        (traineeName != null ? "AND tr.trainee.user.username = :traineeName" : ""), Training.class)
                .setParameter("username", username)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .setParameter("traineeName", traineeName)
                .getResultList();
    }

    public Optional<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        String jpql = "SELECT tr FROM Trainer tr WHERE tr.user.firstName = :firstName AND tr.user.lastName = :lastName";
        TypedQuery<Trainer> query = entityManager.createQuery(jpql, Trainer.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        List<Trainer> results = query.getResultList();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
