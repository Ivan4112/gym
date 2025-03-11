package cucumber.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.controller.TrainingController;
import org.edu.fpm.gym.dto.training.ExternalTrainingServiceDTO;
import org.edu.fpm.gym.entity.Trainer;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.edu.fpm.gym.utils.ActionType;
import org.edu.fpm.gym.utils.TestDataFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TrainingSearchStepDefinition {
    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TrainingController trainingController;

    private String username;

    private ResponseEntity<List<ExternalTrainingServiceDTO>> response;
    @Given("a logged-in trainer {string}")
    public void aLoggedInTrainer(String username) {
        this.username = username;
    }

    @Given("a logged-in trainer {string} with no scheduled trainings")
    public void aLoggedInTrainerWithNoScheduledTrainings(String username) {
        this.username = username;
    }

    /*@When("the trainer searches for their training sessions")
    public void theTrainerSearchesForTheirTrainingSessions() {
        Trainer trainer = TestDataFactory.createTrainer(TestDataFactory.createUser(this.username), TestDataFactory.createTrainingType());

        List<ExternalTrainingServiceDTO> trainings = List.of(
                new ExternalTrainingServiceDTO(
                        username, "FirstName", "LastName", true,
                        LocalDate.now(), 2, ActionType.ADD));
        System.out.println("username " + username);

        when(trainerService.getTrainerByUsername(username)).thenReturn(trainer);
        when(trainingService.getTrainingsByTrainer(trainer.getId())).thenReturn(ResponseEntity.ok(trainings));

        if (username.equals("test.lastName7")) {
            log.info("Searching for training sessions");
            when(trainingService.getTrainingsByTrainer(anyInt()))
                    .thenReturn(ResponseEntity.ok(Collections.singletonList(new ExternalTrainingServiceDTO(
                            username, "test", "lastName7", true,
                            LocalDate.now().plusDays(2), 2, ActionType.ADD
                    ))));
        } else {
            when(trainingService.getTrainingsByTrainer(anyInt()))
                    .thenReturn(ResponseEntity.noContent().build());
        }
        System.out.println("trainer searches for their training sessions");
        response = trainingController.findTrainingByTrainerUsername(username);
        System.out.println("response: " + response);
    }*/

    @Then("the system should return a list of their scheduled trainings")
    public void theSystemShouldReturnListOfTheirScheduledTrainings() {
        assertTrue(response.getBody() != null && !response.getBody().isEmpty(),
                "The response should contain a list of training sessions");
    }

    @Then("the system should return an empty list")
    public void theSystemShouldReturnEmptyList() {
        assertEquals(ResponseEntity.noContent().build(),
                response, "The response should be no content");
    }
}
