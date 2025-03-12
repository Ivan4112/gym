package cucumber.integration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.edu.fpm.gym.GymApplication;
import org.edu.fpm.gym.dto.auth.LoginRequest;
import org.edu.fpm.gym.dto.training.AddTrainingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;

import static org.edu.fpm.gym.utils.ApiPaths.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GymApplication.class)
public class GymTrainerIntegrationSteps {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;
    @LocalServerPort
    private int port;

    private ResponseEntity<String> response;
    private Integer trainingId;

    @Given("the GymService is running")
    public void theGymServiceIsRunning() {
        String token = authenticateAndGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);


    }

    private String authenticateAndGetToken() {
        String fullUrl = "http://localhost:" + port + "/" + AUTH + "/login";

        LoginRequest loginRequest = new LoginRequest("test.lastName7", "UJeZDn75Kx");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        return response.getBody();
    }

    @Given("the TrainerWorkloadService is running")
    public void theTrainerWorkloadServiceIsRunning() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            System.out.println("ActiveMQ is working!");
        } catch (JMSException e) {
            throw new AssertionError("Connection error ActiveMQ", e);
        }
    }

    @When("a training is added for trainer {string} with duration {int} minutes")
    public void TrainingIsAdded(String trainerUsername, int duration) {
        String token = authenticateAndGetToken();
        System.out.println("Token: " + token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        AddTrainingDTO requestObj = new AddTrainingDTO("alex.johnson", trainerUsername, "Cardio",
                LocalDate.now().plusDays(1), duration);

        System.out.println("Request Object: " + requestObj);

        String fullUrl = "http://localhost:" + port + "/v1/gym/training/add";

        try {
            response = restTemplate.exchange(fullUrl, HttpMethod.POST, new HttpEntity<>(requestObj, headers), String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                System.out.println("Training added successfully.");
            } else {
                System.out.println("Failed to add training. Status code: " + response.getStatusCode());
            }

            assertEquals(HttpStatus.CREATED, response.getStatusCode());

        } catch (ResourceAccessException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
            throw e;
        }
    }

    @Then("TrainerWorkloadService should receive the training event")
    public void trainerWorkloadServiceShouldReceiveTheTrainingEvent() {
        String receivedMessage = (String) jmsTemplate.receiveAndConvert("trainer-workload-queue");
        assertNotNull(receivedMessage);
        assertTrue(receivedMessage.contains("trainerUsername"));
    }

    @Given("a training exists for trainer {string} with ID {int}")
    public void TrainingExists(String trainerUsername, int id) {
        this.trainingId = id;
    }

    @When("the training with ID {int} is deleted")
    public void theTrainingIsDeleted(int id) {
        String token = authenticateAndGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        String fullUrl = "http://localhost:" + port + "/" + TRAINING + "/delete?id_training=" + id;
        response = restTemplate.exchange(fullUrl, HttpMethod.DELETE, new HttpEntity<>(headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("TrainerWorkloadService should receive the training deletion event")
    public void trainerWorkloadServiceShouldReceiveTheTrainingDeletionEvent() {
        String receivedMessage = (String) jmsTemplate.receiveAndConvert("trainer-workload-queue");
        assertNotNull(receivedMessage);
    }

    @Given("the trainer {string} has completed trainings")
    public void theTrainerHasCompletedTrainings(String trainerUsername) {
    }

    @When("the workload summary is requested for {string}")
    public void theWorkloadSummaryIsRequested(String trainerUsername) {
        String token = authenticateAndGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        String fullUrl = "http://localhost:" + port + "/" + TRAINER + "/monthly-summary?username=" + trainerUsername;

        response = restTemplate.exchange(fullUrl, HttpMethod.GET, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Then("the response should contain workload statistics")
    public void theResponseShouldContainWorkloadStatistics() {
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("test.lastName7"));
    }
}
