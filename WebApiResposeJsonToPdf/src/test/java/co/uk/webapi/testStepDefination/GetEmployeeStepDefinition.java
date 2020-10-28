package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.EmployeeModel;
import com.aventstack.extentreports.ReportConfigurator;
import com.sun.javafx.font.FontFactory;
import io.cucumber.core.gherkin.vintage.internal.gherkin.deps.com.google.gson.Gson;
import io.cucumber.core.gherkin.vintage.internal.gherkin.deps.com.google.gson.GsonBuilder;
import io.cucumber.core.gherkin.vintage.internal.gherkin.deps.com.google.gson.JsonElement;
import io.cucumber.core.gherkin.vintage.internal.gherkin.deps.com.google.gson.JsonParser;
import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.internal.difflib.Chunk;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import javafx.scene.text.Font;
import org.bson.Document;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/////////////// USING API VERB GET(); ///////////////


public class GetEmployeeStepDefinition {

    private RequestSpecification requestSpec;
    private Response getResponse;
    String cType;
    private int postResponseId;
    private int getResponseId;



    @Given("I have access to a base file")
    public void iHaveAccessToABaseFile() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();


    }

    @Given("I have access to the web api URI and headers input is defined")
    public void iHaveAccessToTheWebApiURIAndHeadersInputIsDefined() {
        this.requestSpec = RestAssured.given().accept(cType);
        //we need requestSpec below so we make it instance variable at the top and assign using this.
        //note we can remove RestAssured and them import it statically in the import
    }

    @When("I Create and Get employee")
    public void iCreateAndGetEmployee(DataTable dataTable) {
     /////CREATE
        //bring the feature file data in here
        List<List<String>> data = dataTable.asLists();  // help to convert dataTable to String
        //Gets data row index, column index.
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3)); //data coming is string so we need to convert to int

        // adding data to PostEmployeeModel. this will make up a body for our POST/CREATE
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setId(0);
        employeeModel.setFirstname(dataFirstname);
        employeeModel.getLastname(dataSurename);
        employeeModel.setGender(dataGender);
        employeeModel.setSalary(dataSalary);
        //Creating new Employee: using data we deSerialized
        //we can pass our URI inside the post(), but the hooks has already taken care of it
        Response postResponse = requestSpec.body(employeeModel).when().post();

        String postResponseString = postResponse.thenReturn().asString(); // return response as string
        //get the ID from jsonPath after posting/CREATING and use it to get by id
        JsonPath jsonPath = new JsonPath(postResponseString);
        this.postResponseId = jsonPath.get("ID");

     ////GET
        this.getResponse = requestSpec.when().get(Integer.toString(postResponseId)); // API Verb get() something from API URI. see hooks class

    }

    @Then("The response should contain data below")  //dataTable is used to validate data in feature file
    public void theResponseShouldContainDataBelow(DataTable dataTable) { // API gives response

        try {
            //// bring the feature file data in here so we can compare with the json response below, using Assertion
            List<List<String>> data = dataTable.asLists();  // help to convert dataTable to String
            //Gets data row index, column index.

            String dataFirstname = data.get(1).get(0);
            String dataSurename = data.get(1).get(1);
            String dataGender = data.get(1).get(2);
            int dataSalary = Integer.parseInt(data.get(1).get(3)); //data coming is string so we need to convert to int




////
            String getResponseString = getResponse.thenReturn().asString(); // return response as string

        /*helps to convert the string responseString object so we can get anything we need. JsonPath has all
        resources in the URI we searched for. dependency in pom.xml
        JSON PATH TO GET ANYTHING FROM RESOURCES
    EXAMPLE:
        {
         "email":"test@hascode.com",
         "firstName":"Tim",
         "id":"2",
         "lastName":"Testerman"
}
     */
            JsonPath jsonPath = new JsonPath(getResponseString);
            getResponseId = jsonPath.get("ID");
            String firstname = jsonPath.get("Firstname");
            String surname = jsonPath.get("Surname");
            String gender = jsonPath.get("Gender");
            int salary = jsonPath.get("Salary");


            //convert json to pdf




            //// Assertion for Cucumber data and JsonPath (scheme) data
            assertEquals(postResponseId,getResponseId);    // or
            assertTrue(dataFirstname.equalsIgnoreCase(firstname));
            assertEquals(dataSurename,surname);
            assertEquals(dataGender,gender);
            assertEquals(dataSalary,salary);


            ////We can check all Responses like body, cookies, headers, test results, status code, time and size
            getResponse.thenReturn().body();
            getResponse.thenReturn().time();
            getResponse.thenReturn().contentType();
            getResponse.thenReturn().cookies();
            //Status code
            assertEquals(getResponse.thenReturn().statusCode(), 200);

            //headers
            Headers headers = getResponse.thenReturn().headers();
            List<Header> header = headers.asList();

            for (Header head : header){
                String headerNames = head.getName();
                String headerValues = head.getValue();
                if (headerNames.equalsIgnoreCase("content-type")){
                    assertEquals(headerValues,"text/html;charset=UTF-8");
                }
                else if (headerNames.equalsIgnoreCase("Connection")){
                    assertEquals(headerValues,"keep-alive");  // we can do for the rest
                }
            }
        }finally {
            ///// Deleting created data

            String id = Integer.toString(getResponseId);
            Response deleteResponse = requestSpec.when().delete(id);
            assertEquals(deleteResponse.statusCode(),204);
        }
    }
}
