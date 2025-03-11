package cucumber.integration;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.edu.fpm.gym.GymApplication;
import org.edu.fpm.gym.dto.auth.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.edu.fpm.gym.utils.ApiPaths.AUTH;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GymApplication.class)
public class AuthStepDefinition {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String username;
    private String password;
    private String authToken;

    @Given("a registered user with username {string} and password {string}")
    public void aRegisteredUserWithUsernameAndPassword(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @When("the user attempts to log in with valid credentials")
    public void theUserAttemptsToLogInWithValidCredentials() {
        String fullUrl = "http://localhost:" + port + "/" + AUTH + "/login";
        LoginRequest loginRequest = new LoginRequest(username, password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer ");
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, entity, String.class);
        authToken = response.getBody();
    }

    @Then("the system should return a valid authentication token")
    public void theSystemShouldReturnAValidAuthenticationToken() {
        log.info("Received token: {}", authToken);
        assertNotNull(authToken);
    }
}
