package org.edu.fpm.gym.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.repository.TraineeRepository;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.repository.TrainingRepository;
import org.edu.fpm.gym.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingTypeRepository trainingTypeRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TraineeRepository traineeRepository,
                           TrainerRepository trainerRepository, TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    public String addTraining(AddTrainingDTO addTrainingDTO) {
        log.info("Adding training: {} for trainee: {} with trainer: {}", addTrainingDTO.trainingName(),
                addTrainingDTO.traineeUsername(), addTrainingDTO.trainerUsername());

        var trainee = traineeRepository.findTraineeByUser_Username(addTrainingDTO.traineeUsername());
        var trainer = trainerRepository.findTrainerByUser_Username(addTrainingDTO.trainerUsername());
        var trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(addTrainingDTO.trainingName());

        var training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName(addTrainingDTO.trainingName());
        training.setTrainingDate(addTrainingDTO.trainingDate());
        training.setTrainingDuration(addTrainingDTO.trainingDuration());
        training.setTrainingType(trainingType);

        trainingRepository.save(training);
        return "Training added successfully";
    }

    public List<Training> getTrainingsByTrainee(Long traineeId) {
        log.info("Fetching trainings for trainee with ID: {}", traineeId);
        List<Training> trainingList = trainingRepository.findByTraineeId(traineeId);
        log.info("Found {} trainings for trainee with ID: {}", trainingList.size(), traineeId);

        return trainingList;
    }

    public List<Training> getTrainingsByTrainer(Long trainerId) {
        List<Training> trainingList = trainingRepository.findByTrainerId(trainerId);
        log.info("Found {} trainings for trainer with ID: {}", trainingList.size(), trainerId);
        return trainingList;
    }
}
