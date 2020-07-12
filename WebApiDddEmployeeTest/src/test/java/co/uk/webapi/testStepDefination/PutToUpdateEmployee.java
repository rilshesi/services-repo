package co.uk.webapi.testStepDefination;

import co.uk.webapi.basesClass.BaseClass;
import co.uk.webapi.schemeModel.EmployeeModel;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class PutToUpdateEmployee {


    String cType;
    private RequestSpecification requestSpec;
    protected Response putResponse;
    private int putResponseid;
    private int putResponseId;


    @Given("I have access to base class1")
    public void iHaveAccessToBaseClass1() throws IOException {
        BaseClass baseClass = new BaseClass();
        cType = baseClass.getProperties();
    }


    @Given("I have access to the web API or PUT")
    public void iHaveAccessToTheWebAPIOrPUT() {
        //this.requestSpec = RestAssured.given().contentType(cType);
        this.requestSpec = given().contentType(cType);
        //note we can remove RestAssured and them import it statically in the import

    }

    @When("I post and put an employee in the web API")
    public void iPostAndPutAnEmployeeInTheWebAPI(DataTable dataTable) {
/////POST
        //getting data to post from feature file
        List<List<String>> data = dataTable.asLists();
        //Gets data row index, column index.
        String dataFirstname = data.get(1).get(0);
        String dataSurename = data.get(1).get(1);
        String dataGender = data.get(1).get(2);
        int dataSalary = Integer.parseInt(data.get(1).get(3));


        // adding data to PostEmployeeModel. this will make up a body for our POST
        EmployeeModel postModel = new EmployeeModel();
        postModel.setId(0);
        postModel.setFirstname(dataFirstname);
        postModel.getLastname(dataSurename);
        postModel.setGender(dataGender);
        postModel.setSalary(dataSalary);


        //Updating Employee with ID=10: using data we deSerialized
        //we can pass our URI inside the put(), but the hooks has already taken care part of URI
        Response postResponse = requestSpec.body(postModel).when().post();


/////PUT
        String responseString = postResponse.thenReturn().asString(); // return response as string
        JsonPath jsonPath = new JsonPath(responseString);
        this.putResponseId = jsonPath.get("ID");

        // new data to PUT
        EmployeeModel putModel = new EmployeeModel();
        putModel.setFirstname("Kunle");
        putModel.getLastname("Odua");
        putModel.setGender("Female");
        putModel.setSalary(50000);
        this.putResponse = requestSpec.body(putModel).when().put(Integer.toString(putResponseId));
    }

    @Then("Employee should be updated and deleted after")
    public void employeeShouldBeUpdatedAndDeletedAfter() {
        try {
            // we need to compare updated details with updated response


            //Getting PUT response and process it as jsonPath and assert with above data
            String putResponseString = putResponse.thenReturn().asString(); // return response as string
            JsonPath jsonPath = new JsonPath(putResponseString);
            this.putResponseid = jsonPath.get("ID");
            String putResponseFirstname = jsonPath.get("Firstname");
            String putResponseSurname = jsonPath.get("Surname");
            String putResponseGender = jsonPath.get("Gender");
            int putResponseSalary = jsonPath.get("Salary");


            //// Assertion for Cucumber data and JsonPath (scheme) data
            assertEquals(putResponseId,putResponseid);
            assertTrue(putResponseFirstname.equalsIgnoreCase("Kunle"));
            assertTrue(putResponseSurname.equalsIgnoreCase("Odua"));
            assertTrue(putResponseGender.equalsIgnoreCase("Female"));
            TestCase.assertEquals(5000, putResponseSalary);


            ////We can check all Responses like body, cookies, headers, test results, status code, time and size
            putResponse.thenReturn().body();
            putResponse.thenReturn().time();
            putResponse.thenReturn().contentType();
            putResponse.thenReturn().cookies();
            //Status code
            assertEquals(putResponse.thenReturn().statusCode(), 200);

            //headers
            Headers headers = putResponse.thenReturn().headers();
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

            String id = Integer.toString(putResponseid);
            Response deleteResponse = requestSpec.when().delete(id);
            assertEquals(deleteResponse.statusCode(),204);
        }




    }
}
