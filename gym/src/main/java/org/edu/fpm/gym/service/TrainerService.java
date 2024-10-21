package org.edu.fpm.gym.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.repository.TrainerRepository;
import org.edu.fpm.gym.security.SecurityCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;

    private SecurityCredential securityCredential;

    @Autowired
    public void setSecurityCredential(SecurityCredential securityCredential) {
        this.securityCredential = securityCredential;
    }

    public Trainer createTrainer(Trainer trainer) {
        trainer.getUser().setUsername(securityCredential.generateUsername(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getId())
        );
        trainer.getUser().setPassword(securityCredential.generatePassword());
        return trainerRepository.save(trainer);
    }

    public Trainer getTrainerByUsername(String username) {
        return trainerRepository.findByUsername(username);
    }

    public void updateTrainerProfile(Trainer trainer) {
        trainerRepository.updateTrainer(trainer);
    }

    public void changeTrainerPassword(String username, String newPassword) {
        trainerRepository.changePassword(username, newPassword);
    }

    public void switchTrainerActivation(String username) {
        trainerRepository.switchActivation(username);
    }

    public void deleteTrainer(String username) {
        trainerRepository.deleteByUsername(username);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String traineeUsername) {
        return trainerRepository.findAvailableTrainersForTrainee(traineeUsername);
    }

    public boolean existsByFullName(String firstName, String lastName) {
        return trainerRepository.findByFirstNameAndLastName(firstName, lastName).isPresent();
    }
}
