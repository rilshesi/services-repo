package uk.co.manchester;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class EmployeeRetrieval3Test extends HooksTest{

    // JSON PATH TO GET ANYTHING FROM RESOURCES
    /* EXAMPLE:
        {
         "email":"test@hascode.com",
         "firstName":"Tim",
         "id":"1",
         "lastName":"Testerman"
}
     */

    @Test
    public void checkAllEmps(){

        RestAssured.given().when().then();  // is the basic structure of restAssured

        //or

        String jsonObject =RestAssured
                .given()
 //                   .contentType("application/json")  //or
                    .accept("application/json")
                .when()         //when() i get(uri)
                    .get()    // base path in Hooks and extends above
                .thenReturn().toString();  // convert to string so we can get Json object

        JsonPath jsonPath = new JsonPath(jsonObject);
        Object firstNames = jsonPath.get("firstName");    // this will get all first name in an array from Employees
        Object emails = jsonPath.get("email");

        //we can get more from the resources
    }
}
