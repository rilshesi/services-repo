package uk.co.manchester;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class EmployeeRetrievalTest {

    @Test
    public void checkAllEmps(){

        RestAssured.given().when().then();  // is the basic structure of restAssured

        //or

        Response response =RestAssured
                .given()
 //                   .contentType("application/json")  //or
                    .accept("application/json")
                .when()         //when() i get(uri)
                    .get("http://localhost:64587/api/Employees/61");

                String responseString = response.toString(); //convert to string
                System.out.println(responseString); // copy and past value in notepad
    }
}
