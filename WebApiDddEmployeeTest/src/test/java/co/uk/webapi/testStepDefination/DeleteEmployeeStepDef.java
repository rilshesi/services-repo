package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class DeleteEmployeeStepDef {

    String cType;
    private DataTable dataTable;
    private RequestSpecification requestSpec;
    private Response response;



    @Given("I have access to a base class")
    public void iHaveAccessToABaseClass() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();
    }

    @Given("I have access to the web API or Delete")
    public void iHaveAccessToTheWebAPIOrDelete() {
        //this.requestSpec = RestAssured.given().contentType(cType);
        this.requestSpec = given().contentType(cType);
        //note we can remove RestAssured and them import it statically in the import
    }

    @When("I delete an employee in the web API with {string}")
    //// NOTE THAT YOU SHOULD CREATE HERE BEFORE DELETE as we assume no data in database
    public void iDeleteAnEmployeeInTheWebAPIWith(String string) {
        this.response = requestSpec.when().delete(string);
    }

    @Then("There should be no details")
    public void thereShouldBeNoDetails() {
        Assert.assertEquals(response.statusCode(), 202); //assert for response equal 202
    }

}
