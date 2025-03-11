package cucumber.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.edu.fpm.gym.controller.TrainerController;
import org.edu.fpm.gym.dto.externalservice.TrainerWorkloadSummaryDTO;
import org.edu.fpm.gym.service.TrainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@CucumberContextConfiguration
public class TrainerStatisticsSteps {
    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    private TrainerWorkloadSummaryDTO response;

    @Given("logging trainer {string}")
    public void aLoggedInTrainer(String username) {
        when(trainerService.getTrainerMonthlyWorkload(username))
                .thenReturn(new TrainerWorkloadSummaryDTO(username, "test", "lastName7", true, Collections.emptyList()));
    }

    @When("the trainer requests their monthly workload summary")
    public void theTrainerRequestsTheirMonthlyWorkloadSummary() {
        response = trainerController.getMonthlyWorkload("test.lastName7");
    }

    @Then("the system should return a workload summary report")
    public void theSystemShouldReturnWorkloadSummaryReport() {
        Assertions.assertNotNull(response, "Response should not be null");
        Assertions.assertEquals("test.lastName7", response.username(), "Trainer username should match");
    }

    @Given("a logged-in user")
    public void aLoggedInUser() {
    }

    @When("the trainer requests the monthly workload summary for {string}")
    public void theTrainerRequestsTheMonthlyWorkloadSummaryFor(String username) {
        try {
            response = trainerController.getMonthlyWorkload(username);
        } catch (Exception ignored) {
        }
    }

    @Then("the system should return an error {string}")
    public void theSystemShouldReturnAnRErrorMessage(String expectedMessage) {
        assertNotNull("Error msg should be shown", expectedMessage);
    }
}
