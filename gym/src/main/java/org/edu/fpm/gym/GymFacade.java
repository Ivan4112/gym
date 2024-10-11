package org.edu.fpm.gym;

import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GymFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public GymFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public Trainee createTrainee(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    public void deleteTrainee(Long id) {
        traineeService.deleteTrainee(id);
    }

    public Trainee getTraineeById(Long id) {
        return traineeService.getTraineeById(id);
    }

    // Методи для Trainer
    public Trainer createTrainer(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public Trainer getTrainerById(Long id) {
        return trainerService.getTrainerById(id);
    }

    // Методи для Training
    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public Training getTrainingById(Long id) {
        return trainingService.getTrainingById(id);
    }
}
