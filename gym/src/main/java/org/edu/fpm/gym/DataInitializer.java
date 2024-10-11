package org.edu.fpm.gym;

import jakarta.annotation.PostConstruct;
import org.edu.fpm.gym.dao.TraineeDao;
import org.edu.fpm.gym.dao.TrainerDao;
import org.edu.fpm.gym.dao.TrainingDao;
import org.edu.fpm.gym.entity.Trainee;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.entity.Training;
import org.edu.fpm.gym.entity.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

@Configuration
@PropertySource("classpath:application.properties")
public class DataInitializer {
    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private TrainingDao trainingDao;

    @Value("${trainer.data.file}")
    private String trainerFilePath;

    @Value("${trainee.data.file}")
    private String traineeFilePath;

    @Value("${training.data.file}")
    private String trainingFilePath;

    @PostConstruct
    public void init() {
        initTraineeData();
        initTrainerData();
        initTrainingData();
    }

    public void initTraineeData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(traineeFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainee trainee = new Trainee(
                        Long.parseLong(data[0]), data[1],
                        data[2], data[3], data[4],
                        Boolean.getBoolean(data[5]),
                        LocalDate.parse(data[6]), data[7]);
                traineeDao.save(trainee);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTrainerData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(traineeFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Trainer trainer = new Trainer(
                        Long.parseLong(data[0]), data[1],
                        data[2], data[3], data[4],
                        Boolean.getBoolean(data[5]), data[6]);
                trainerDao.save(trainer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTrainingData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(traineeFilePath))) {
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
                trainingDao.save(training);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
