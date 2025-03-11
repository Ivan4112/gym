package cucumber.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.edu.fpm.gym.GymApplication;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = GymApplication.class)
public class CucumberTestConfig {
}
