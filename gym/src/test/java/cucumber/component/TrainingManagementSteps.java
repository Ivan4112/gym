package cucumber.component;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.edu.fpm.gym.controller.TrainingController;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.edu.fpm.gym.service.TrainerService;
import org.edu.fpm.gym.service.TrainingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainingManagementSteps {
    @Mock
    private TrainingService trainingService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainingController trainingController;

    private ResponseEntity<String> response;

    private Exception exception;

    private AddTrainingDTO request = new AddTrainingDTO("traineeUsername",
            "test.lastName7", "GYM",
            LocalDate.now().plusDays(2), 3);

    public TrainingManagementSteps() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("logg-in trainer {string}")
    public void loggedInTrainer(String username) {
        when(trainerService.getTrainerMonthlyWorkload(username)).thenReturn(null);
    }

    @When("the trainer adds a training session with details:")
    public void theTrainerAddsTrainingSessionWithDetails(DataTable dataTable) {
        var data = dataTable.asMaps().getFirst();
        when(trainingService.addTraining(request)).thenReturn("Training added successfully.");
        response = ResponseEntity.status(201).body(trainingController.addTraining(request));
    }

    @Then("the system should confirm the training session was added")
    public void theSystemShouldConfirmTheTrainingSessionWasAdded() {
        assertNotNull(response);
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Training added successfully.", response.getBody());
    }

    @When("the trainer attempts to add a training session with missing client")
    public void theTrainerAttemptsToAddTrainingSessionWithMissingClient() {
        when(trainingService.addTraining(request)).thenThrow(new IllegalArgumentException("Client cannot be null"));

        try {
            response = ResponseEntity.status(201).body(trainingController.addTraining(request));
        } catch (IllegalArgumentException e) {
            exception = e;
        }
    }

    @Then("the system should return an error message {string}")
    public void theSystemShouldReturnErrorMessage(String expectedMessage) {
        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Given("an existing training session with ID {string}")
    public void anExistingTrainingSessionWithId(String id) {
        doNothing().when(trainingService).deleteTraining(Integer.parseInt(id));
    }

    @When("the trainer deletes the training session")
    public void theTrainerDeletesTheTrainingSession() {
        response = trainingController.deleteTraining(10);
    }

    @Then("the system should confirm the training session was deleted")
    public void theSystemShouldConfirmTheTrainingSessionWasDeleted() {
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Training deleted successfully.", response.getBody());
    }

    @When("the trainer attempts to delete a training session with ID {string}")
    public void theTrainerAttemptsToDeleteTrainingSessionWithId(String id) {
        doThrow(new IllegalStateException("Training session not found")).when(trainingService).deleteTraining(Integer.parseInt(id));
        try {
            response = trainingController.deleteTraining(Integer.parseInt(id));
        } catch (Exception e) {
            exception = e;
        }
    }
}
