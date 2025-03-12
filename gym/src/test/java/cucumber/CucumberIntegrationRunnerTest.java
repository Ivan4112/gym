package cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/integration",
        plugin = {"pretty", "html:target/cucumber-integration-report.html"},
        glue = "cucumber.integration",
        monochrome = true,
        tags = "@integrationTest"
)
public class CucumberIntegrationRunnerTest {
}
