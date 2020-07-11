package co.uk.webapi;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


@RunWith(Cucumber.class)
@CucumberOptions(
        dryRun = false   //Does not run any code content in the step definition when it is true
        ,monochrome = true  //make text on the console readable after running feature file for the first time
        ,plugin = {"pretty",
                   "html:target/employee-test-report/report-html",
                   "json:target/employee-test-report/report-json.json",
                   "junit:target/employee-test-report/report-xml.xml"}    //report template
        ,strict = false  // Skip execution of pending and undefined steps it true
        ,features ="src/test/resources"  //package where the feature file are located
        ,snippets = CucumberOptions.SnippetType.CAMELCASE   //used to determine what the method of strp def would be
//        ,glue ={"testStepDefination"}   //package with step definitions and hooks
        ,tags = {"@get"}
)
public class ApiRunnerTest {
}
