package test;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions(
		features = "classpath:features",
		plugin = {"pretty", "html:target/site/cucumber-pretty",
		        "json:target/cucumber.json"},
		tags = {"@ePADD"}
		)
public class Test{
	
}