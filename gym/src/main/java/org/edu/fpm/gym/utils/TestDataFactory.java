package org.edu.fpm.gym.utils;

import org.edu.fpm.gym.dto.trainee.TraineeDTO;
import org.edu.fpm.gym.dto.trainer.TrainerProfileDTO;
import org.edu.fpm.gym.dto.training.TrainingDTO;
import org.edu.fpm.gym.dto.training.TrainingRequestDTO;
import org.edu.fpm.gym.dto.trainingType.TrainingTypeDTO;
import org.edu.fpm.gym.dto.user.UserDTO;
import org.edu.fpm.gym.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TestDataFactory {
    public static User createUser(String username) {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername(username);
        user.setPassword("password");
        user.setIsActive(true);
        return user;
    }

    public static TrainingType createTrainingType() {
        return new TrainingType(1 ,"Cardio");
    }

    public static Trainer createTrainer(User user, TrainingType specialization) {
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        trainer.setTrainees(new HashSet<>());
        return trainer;
    }

    public static List<Trainer> createTrainerList() {
        User user1 = createUser("John.Doe");
        User user2 = createUser("Jane.Smith");

        TrainingType specialization1 = createTrainingType();
        TrainingType specialization2 = createTrainingType();

        Trainer trainer1 = createTrainer(user1, specialization1);
        Trainer trainer2 = createTrainer(user2, specialization2);

        return Arrays.asList(trainer1, trainer2);
    }

    public static TrainerProfileDTO createTrainerProfileDTO() {
        TrainingType trainingType = createTrainingType();
        return new TrainerProfileDTO("John", "Doe", trainingType, true, new ArrayList<>());
    }

    public static TrainingDTO createTrainingDTO(String trainingName, String trainerUsername) {
        TrainingType trainingType = createTrainingType();
        return new TrainingDTO(trainingName, LocalDate.now(), trainingType, 60, trainerUsername);
    }

    public static List<TrainingDTO> createTrainingDTOList() {
        return List.of(
                createTrainingDTO("Yoga", "john.doe"),
                createTrainingDTO("Pilates", "john.doe")
        );
    }

    public static TrainingTypeDTO createTrainingTypeDTO(int id, String name) {
        return new TrainingTypeDTO(id, name);
    }

    public static List<TrainingTypeDTO> createTrainingTypeDTOList() {
        return List.of(
                createTrainingTypeDTO(1, "Yoga"),
                createTrainingTypeDTO(2, "Pilates")
        );
    }

    public static Trainee createTrainee(User user) {
        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("123 Main St");
        return trainee;
    }

    public static TraineeDTO createTraineeDTO() {
        return new TraineeDTO(
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                new UserDTO("John", "Doe", "johndoe", true)
        );
    }

    public static Training createTraining(Trainer trainer) {
        Training training = new Training();
        training.setTrainingName("Cardio Workout");
        training.setTrainingDate(LocalDate.now());
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());
        return training;
    }

    public static TrainingRequestDTO createTrainingRequestDTO(){
        return new TrainingRequestDTO("john.doe", LocalDate.of(2024, 10, 10),
                LocalDate.of(2024, 11, 14), "trainerName", createTrainingType(), "password");
    }
}
