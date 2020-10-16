package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.EmployeeModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PostEmployeeStepDef {

    String cType;
    private DataTable dataTable;
    private RequestSpecification requestSpec;
    private Response postResponse;


    @Given("I have access to base class")
    public void iHaveAccessToBaseClass() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();
    }

    @Given("I have access to the web API")
    public void iHaveAccessToTheWebAPI() {
        this.requestSpec = RestAssured.given().contentType(cType);
        //note we can remove RestAssured and them import it statically in the import
    }

    @When("I create an employee in the web API")
    public void iCreateAnEmployeeInTheWebAPI(io.cucumber.datatable.DataTable dataTable) {
        //getting data to post from feature file
        this.dataTable=dataTable;
        List<List<String>> data = dataTable.asLists();
        //Gets data row index, column index.
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3));


        // adding data to PostEmployeeModel. this will make up a body for our POST
        EmployeeModel employeeModel = new EmployeeModel();
        employeeModel.setFirstname(dataFirstname);
        employeeModel.getLastname(dataSurename);
        employeeModel.setGender(dataGender);
        employeeModel.setSalary(dataSalary);


        //Creating new Employee: using data we deSerialized
        //we can pass our URI inside the post(), but the hooks has already taken care of it
        this.postResponse = requestSpec.body(employeeModel).when().post();


    }

    @Then("Employee should be created as above and deleted")
    public void employeeShouldBeCreatedAsAboveAndDeleted(int idToDelete) {

        try {   // compare postResponse with data in feature file
            List<List<String>> data = dataTable.asLists();
            String dataFirstname = data.get(1).get(0);
            String dataSurename = data.get(1).get(1);
            String dataGender = data.get(1).get(2);
            int dataSalary = Integer.parseInt(data.get(1).get(3));

            //Getting response and process it as jsonPath and assert with above data
            String postResponseString = postResponse.thenReturn().asString(); // return response as string
            JsonPath jsonPath = new JsonPath(postResponseString);
            idToDelete = jsonPath.get("ID"); //NOTE ID IS AUTOMATICALLY CREATED IN DATABASE WHEN WHEN POST, but we
            //   can get that id created by DB and use it in the delete process
            String firstname = jsonPath.get("Firstname");
            String surname = jsonPath.get("Surname");
            String gender = jsonPath.get("Gender");
            int salary = jsonPath.get("Salary");


            //// Assertion for Cucumber data and JsonPath (scheme) data
            //   assertEquals(dataId,id);    // or
            assertTrue(dataFirstname.equalsIgnoreCase(firstname));
            assertEquals(dataSurename,surname);
            assertEquals(dataGender,gender);
            assertEquals(dataSalary,salary);


            ////We can check all Responses like body, cookies, headers, test results, status code, time and size
            postResponse.thenReturn().body();
            postResponse.thenReturn().time();
            postResponse.thenReturn().contentType();
            postResponse.thenReturn().cookies();
            //Status code
            assertEquals(postResponse.thenReturn().statusCode(), 201);

            //headers
            Headers headers = postResponse.thenReturn().headers();
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
            String id = Integer.toString(idToDelete);
            Response deleteResponse = requestSpec.when().delete(id);
            assertEquals(deleteResponse.statusCode(),204);
        }
    }
}
