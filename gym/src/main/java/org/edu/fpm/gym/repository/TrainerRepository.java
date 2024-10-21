package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository {
    Trainer save(Trainer trainer);
    Trainer findByUsername(String username);
    void updateTrainer(Trainer trainer);
    void changePassword(String username, String newPassword);
    void switchActivation(String username);
    void deleteByUsername(String username);
    List<Trainer> findAvailableTrainersForTrainee(String traineeUsername);
    List<Training> getTrainerTrainings(String username, LocalDate fromDate, LocalDate toDate, String traineeName);
    Optional<Trainer> findByFirstNameAndLastName(String firstName, String lastName);
}
