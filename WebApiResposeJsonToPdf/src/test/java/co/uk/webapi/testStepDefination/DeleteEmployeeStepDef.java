package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.EmployeeModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class DeleteEmployeeStepDef {

    String cType;
    private RequestSpecification requestSpec;
    private Response deleteResponse;


    @Given("I have access to a base class")
    public void iHaveAccessToABaseClass() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();
    }

    @Given("I have access to the web api URI and headers input was defined")
    public void iHaveAccessToTheWebApiURIAndHeadersInputWasDefined() {
        //this.requestSpec = RestAssured.given().contentType(cType);
        this.requestSpec = given().contentType(cType);
        //note we can remove RestAssured and them import it statically in the import
    }

    @When("I create employee and delete response id")
    //// NOTE THAT YOU SHOULD CREATE HERE BEFORE DELETE as we assume no data in database
    public void iCreateEmployeeAndDeleteResponseId(DataTable dataTable) {
////POST
        //getting data to post from feature file
        List<List<String>> data = dataTable.asLists();
        //Gets data row index, column index.
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3));


        // adding data to PostEmployeeModel. this will make up a body for our POST
        EmployeeModel postModel = new EmployeeModel();
        postModel.setFirstname(dataFirstname);
        postModel.getLastname(dataSurename);
        postModel.setGender(dataGender);
        postModel.setSalary(dataSalary);

        //Creating new Employee: using data we deSerialized
        //we can pass our URI inside the post(), but the hooks has already taken care of it
        Response postResponse = requestSpec.body(postModel).when().post();

        //Getting response and process it as jsonPath and assert with above data
        String postResponseString = postResponse.thenReturn().asString(); // return response as string
        JsonPath jsonPath = new JsonPath(postResponseString);
        int idToDelete = jsonPath.get("ID"); //NOTE ID IS AUTOMATICALLY CREATED IN DATABASE WHEN WHEN POST, but we

////DELETE
        this.deleteResponse = requestSpec.when().delete(Integer.toString(idToDelete));
    }

    @Then("The response should contain no data")
    public void theResponseShouldContainNoData() {
        Assert.assertEquals(deleteResponse.statusCode(), 204); //assert for response equal 204
    }

}
