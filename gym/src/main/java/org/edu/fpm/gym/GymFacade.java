package org.edu.fpm.gym;

import org.edu.fpm.gym.entity.*;
import org.edu.fpm.gym.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService, UserService userService, TrainingTypeService trainingTypeService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
        this.userService = userService;
        this.trainingTypeService = trainingTypeService;
    }
    //Trainer
    public String createTrainerProfile(Trainer trainer) {
        return trainerService.createTrainer(trainer).toString();
    }

    public Trainer getTrainerProfileByUsername(String username) {
        return trainerService.getTrainerByUsername(username);
    }

    public void updateTrainerProfile(Trainer trainer) {
        trainerService.updateTrainerProfile(trainer);
    }

    public void activateTrainer(String username) {
        trainerService.switchTrainerActivation(username);
    }

    public void changePasswordTrainer(String username, String password) {
        trainerService.changeTrainerPassword(username, password);
    }

    public void deleteTrainerProfile(String username) {
        trainerService.deleteTrainer(username);
    }

    public List<Trainer> getTraineeTrainings(String traineeUsername) {
        return trainerService.getAvailableTrainersForTrainee(traineeUsername);
    }
    public List<Training> getTrainingsForTrainer(String username, LocalDate fromDate, LocalDate toDate, String traineeName){
        return trainerService.getTrainingsForTrainer(username, fromDate, toDate, traineeName);
    }
    // Trainee
    public String createTraineeProfile(Trainee trainee) {
        return traineeService.createTrainee(trainee).toString();
    }
    public void updateTrainerProfile(Trainee trainee) {
        traineeService.updateTraineeProfile(trainee);
    }
    public Trainee getTraineeProfileByUsername(String username) {
        return traineeService.getTraineeByUsername(username);
    }

    public void updateTraineeProfile(Trainee trainee) {
        traineeService.updateTraineeProfile(trainee);
    }

    public void activateTrainee(String username) {
        traineeService.switchTraineeActivation(username);
    }

    public void changePasswordTrainee(String username, String password) {
        traineeService.changeTraineePassword(username, password);
    }

    public void deleteTraineeByUsername(String username){
        traineeService.deleteTraineeByUsername(username);
    }

    public List<Training> getTrainingsByTrainee(String traineeUsername, LocalDate fromDate, LocalDate toDate, String trainerName, TrainingType trainingType) {
        return traineeService.getTraineeTrainings(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    // Training
    public String addTraining(Training training) {
        return trainingService.createTraining(training).toString();
    }

    public List<Training> getTraineeTrainings(Long traineeId) {
        return trainingService.getTrainingsByTrainee(traineeId);
    }

    public List<Training> getTrainerTrainings(Long traineeId) {
        return trainingService.getTrainingsByTrainer(traineeId);
    }

    //User
    public User createUser(User user){
        userService.createUser(user);
        return user;
    }

    //TrainingType
    public TrainingType addTrainingType(TrainingType trainingType) {
        return trainingTypeService.createTrainingType(trainingType);
    }

    public TrainingType findTrainingTypeByName(String name) {
        return trainingTypeService.findTrainingTypeByName(name);
    }
}
