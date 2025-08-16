package testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
		
		features = "Feature/Login.feature", 
		glue = { "stepDefinitions" }, 
		dryRun = false, 																																																																																								// are																												// defined
		monochrome = true, 
		plugin = { "pretty", "html:target/cucumber-reports" }

)

public class TestRun {

}
