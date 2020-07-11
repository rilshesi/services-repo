package uk.co.manchester;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.basic;

public class HooksTest {  // Hoots can also be a Bass Class

    @BeforeClass
    public static void testInitialize(){

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 64587;
        RestAssured.basePath = "/api/Employees";
//        RestAssured.authentication = basic ("username","password");
//        RestAssured.rootPath = "store/book";
    }
}
