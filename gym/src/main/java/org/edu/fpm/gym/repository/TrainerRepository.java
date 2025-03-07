package org.edu.fpm.gym.repository;

import jakarta.transaction.Transactional;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Trainer findTrainerByUser_Username(String username);
    @Modifying
    @Query("UPDATE Trainer t SET t = :trainer WHERE t.user.username = :username")
    Trainer updateTrainerByUserUsername(@Param("username") String username, @Param("trainer") Trainer trainer);

    @Modifying
    @Transactional
    @Query("UPDATE Trainer t SET t.user.isActive = :isActive WHERE t.user.username = :username")
    void switchActivation(@Param("username") String username, @Param("isActive") boolean isActive);

    void deleteTrainerByUser_Username(String username);

    @Query("SELECT t FROM Trainer t WHERE t.id NOT IN " +
            "(SELECT tr.id FROM Trainee ta JOIN ta.trainers tr WHERE ta.user.username = :username)")
    List<Trainer> findAvailableTrainersForTrainee(@Param("username") String traineeUsername);

    @Query("SELECT tr FROM Training tr WHERE tr.trainer.user.username = :username " +
            "AND tr.trainingDate >= :fromDate AND tr.trainingDate <= :toDate " +
            "AND (:traineeName IS NULL OR tr.trainee.user.username = :traineeName)")
    List<Training> getTrainingsForTrainer(@Param("username") String username,
                                          @Param("fromDate") LocalDate fromDate,
                                          @Param("toDate") LocalDate toDate,
                                          @Param("traineeName") String traineeName);
}
