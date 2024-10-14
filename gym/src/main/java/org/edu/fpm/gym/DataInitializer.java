package org.edu.fpm.gym;

import jakarta.annotation.PostConstruct;
import org.edu.fpm.gym.dao.TrainerDao;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.edu.fpm.gym.service.TraineeService;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.time.LocalDate;

@Configuration
@PropertySource("classpath:application.properties")
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingService trainingService;


    @Value("${trainee.data.file}")
    private Resource traineeFilePath;

    @Value("${trainer.data.file}")
    private Resource trainerFilePath;

    @Value("${training.data.file}")
    private Resource trainingFilePath;

    @PostConstruct
    public void init() {
        logger.info("Initializing data ...");
        initTraineeData();
        initTrainerData();
        initTrainingData();
        logger.info("Data has been initialized");
    }

    public void initTraineeData() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(traineeFilePath.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainee trainee = new Trainee(
                        Long.parseLong(data[0]),
                        data[1], data[2], data[3], data[4],
                        Boolean.parseBoolean(data[5]),
                        LocalDate.parse(data[6]), data[7]);
                traineeService.createTrainee(trainee);
            }
        } catch (IOException e) {
            logger.error("Error initializing trainee data: {}", e.getMessage());
        }
        System.out.println(traineeService.getAllTrainees());

    }

    public void initTrainerData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainerFilePath.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainer trainer = new Trainer(
                        Long.parseLong(data[0]),
                        data[1], data[2], data[3], data[4],
                        Boolean.parseBoolean(data[5]),
                        data[6]);
                trainerService.createTrainer(trainer);
            }
        } catch (IOException e) {
            logger.error("Error initializing trainer data: {}", e.getMessage());
        }
        System.out.println(trainerService.getAllTrainers());
    }

    public void initTrainingData() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainingFilePath.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                TrainingType trainingType = TrainingType.valueOf(data[3].trim().toUpperCase());
                Training training = new Training();

                training.setTraineeId(Long.parseLong(data[0].trim()));
                training.setTrainerId(Long.parseLong(data[1].trim()));
                training.setTrainingName(data[2].trim());
                training.setTrainingType(trainingType);
                training.setTrainingDate(LocalDate.parse(data[4].trim()));
                training.setTrainingDuration(Integer.parseInt(data[5].trim()));
                trainingService.createTraining(training);
            }
        } catch (IOException e) {
            logger.error("Error initializing training data: {}", e.getMessage());
        }
        System.out.println(trainingService.getAllTrainings());
    }
}
