package org.edu.fpm.gym.repository;

import jakarta.transaction.Transactional;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Trainee findTraineeByUser_Username(String username);

    @Modifying
    @Query("UPDATE Trainee t SET t = :trainee WHERE t.user.username = :username")
    void updateTraineeByUserUsername(@Param("username") String username, @Param("trainee") Trainee trainee);

    @Modifying
    @Transactional
    @Query("UPDATE Trainee t SET t.user.isActive = :isActive WHERE t.user.username = :username")
    void switchActivation(@Param("username") String username, @Param("isActive") boolean isActive);

    void deleteTraineeByUser_Username(String username);

    @Query("SELECT tr FROM Training tr JOIN tr.trainee t " +
            "WHERE t.user.username = :username AND tr.trainingDate " +
            "BETWEEN :fromDate AND :toDate AND tr.trainer.user.firstName = :trainerName AND tr.trainingType = :trainingType")
    List<Training> findTrainingsByTraineeAndDateRange(
            @Param("username") String username,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") TrainingType trainingType
    );
}
