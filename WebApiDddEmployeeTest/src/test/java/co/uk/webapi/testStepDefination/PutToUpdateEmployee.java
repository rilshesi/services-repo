package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.PostEmpoyeeModel;
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

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PutToUpdateEmployee {


    String cType;
    private DataTable dataTable;
    private RequestSpecification requestSpec;
    private Response response;


    @Given("I have access to base file")
    public void iHaveAccessToBaseFile() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();
    }


    @Given("I have access to the web API or PUT")
    public void iHaveAccessToTheWebAPIOrPUT() throws IOException {
        //this.requestSpec = RestAssured.given().contentType(cType);
        this.requestSpec = given().contentType(cType);
        //note we can remove RestAssured and them import it statically in the import

    }

    @When("I update an employee in the web API")
    public void iUpdateAnEmployeeInTheWebAPI(io.cucumber.datatable.DataTable dataTable) {

        //getting data to post from feature file
        this.dataTable=dataTable;
        List<List<String>> data = dataTable.asLists();
        //Gets data row index, column index.
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3));


        // adding data to PostEmployeeModel. this will make up a body for our POST
        PostEmpoyeeModel postEmpoyeeModel = new PostEmpoyeeModel();
        postEmpoyeeModel.setId(10);
        postEmpoyeeModel.setFirstname(dataFirstname);
        postEmpoyeeModel.getLastname(dataSurename);
        postEmpoyeeModel.setGender(dataGender);
        postEmpoyeeModel.setSalary(dataSalary);


        //Updating Employee with ID=10: using data we deSerialized
        //we can pass our URI inside the put(), but the hooks has already taken care part of URI
        this.response = requestSpec.body(postEmpoyeeModel).when().put("10");
    }

    @Then("the employee record is updated")
    public void theEmployeeRecordIsUpdated() {
        // we need to compare this with the response from the api below
        List<List<String>> data = dataTable.asLists();
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3));

        //Getting response and process it as jsonPath and assert with above data
        String responseString = response.thenReturn().asString(); // return response as string
        JsonPath jsonPath = new JsonPath(responseString);
        int id = jsonPath.get("ID");
        String firstname = jsonPath.get("Firstname");
        String surname = jsonPath.get("Surname");
        String gender = jsonPath.get("Gender");
        int salary = jsonPath.get("Salary");


        //// Assertion for Cucumber data and JsonPath (scheme) data
        assertEquals(id, 10);    // or
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

    }
}
