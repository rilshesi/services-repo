package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.PostEmpoyeeModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.*;  //* instead of RestAssured
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


/////////////// USING API VERB GET(); ///////////////

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.format;
import static org.junit.Assert.assertEquals;


public class GetEmployeeStepDefinition {

    private RequestSpecification requestSpec;
    private Response response;
    String cType;
    private int responseId;


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
        PostEmpoyeeModel postEmpoyeeModel = new PostEmpoyeeModel();
        postEmpoyeeModel.setId(0);
        postEmpoyeeModel.setFirstname(dataFirstname);
        postEmpoyeeModel.getLastname(dataSurename);
        postEmpoyeeModel.setGender(dataGender);
        postEmpoyeeModel.setSalary(dataSalary);
        //Creating new Employee: using data we deSerialized
        //we can pass our URI inside the post(), but the hooks has already taken care of it
        Response postResponse = requestSpec.body(postEmpoyeeModel).when().post();

        String responseString = postResponse.thenReturn().asString(); // return response as string
        //get the ID from jsonPath after posting/CREATING and use it to get by id
        JsonPath jsonPath = new JsonPath(responseString);
        this.responseId = jsonPath.get("ID");

     ////GET
        this.response = requestSpec.when().get(Integer.toString(responseId)); // API Verb get() something from API URI. see hooks class

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
            String responseString = response.thenReturn().asString(); // return response as string

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
            JsonPath jsonPath = new JsonPath(responseString);
            int id = jsonPath.get("ID");
            String firstname = jsonPath.get("Firstname");
            String surname = jsonPath.get("Surname");
            String gender = jsonPath.get("Gender");
            int salary = jsonPath.get("Salary");


            //// Assertion for Cucumber data and JsonPath (scheme) data
            assertEquals(responseId,id);    // or
            assertTrue(dataFirstname.equalsIgnoreCase(firstname));
            assertEquals(dataSurename,surname);
            assertEquals(dataGender,gender);
            assertEquals(dataSalary,salary);


            ////We can check all Responses like body, cookies, headers, test results, status code, time and size
            response.thenReturn().body();
            response.thenReturn().time();
            response.thenReturn().contentType();
            response.thenReturn().cookies();
            //Status code
            assertEquals(response.thenReturn().statusCode(), 200);

            //headers
            Headers headers = response.thenReturn().headers();
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
            String id = Integer.toString(responseId);
            this.response = requestSpec.when().delete(id);
        }
    }
}
