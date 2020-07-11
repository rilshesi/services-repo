package uk.co.manchester;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class EmployeeRetrieval2Test extends HooksTest{

    @Test
    public void checkAllEmps(){

        RestAssured.given().when().then();  // is the basic structure of restAssured

        //or

        Response response =RestAssured
                .given()
 //                   .contentType("application/json")  //or
                    .accept("application/json")
                .when()         //when() i get(uri). when() is BDD verb
                    .get()    // base path in Hooks is extends above. get() is API verbs
                .thenReturn();  //return responses, like header, status code, body, cookies, time, test result , size
                int statusCode = response.statusCode();                  // gets all Headers same as in postman response
        Assert.assertEquals("Does not contain 200", 200, statusCode);  // assert equal for status
                ResponseBody responseBody = response.getBody();   // you need to get body as String to see it
                String responseBodyString = responseBody.toString();
                String contentType = response.getContentType();
                Headers headers = response.getHeaders(); // we can get more if we need them. you can copy content or
        // loop to get any particular header
    }
}
