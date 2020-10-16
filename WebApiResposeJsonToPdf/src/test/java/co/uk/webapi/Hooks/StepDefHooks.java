package co.uk.webapi.Hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;

public class StepDefHooks {

    @Before
    public void before(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 64587;
        RestAssured.basePath = "/api/Employees/";
//        RestAssured.authentication = basic ("username","password");
//        RestAssured.rootPath = "store/book";
        System.out.println("Before is called");

    }



    @After
    public void after(Scenario scenario){
        System.out.println("After is called");
    }
}
