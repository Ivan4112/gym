package org.edu.fpm.gym.repository;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository {
    Trainee save(Trainee trainee);
    Trainee findByUsername(String username);
    void updateTrainee(Trainee trainee);
    void changePassword(String username, String newPassword);
    void switchActivation(String username);
    void deleteByUsername(String username);
    List<Training> getTraineeTrainings(String username, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType);
    Optional<Trainee> findByFirstNameAndLastName(String firstName, String lastName);
}
