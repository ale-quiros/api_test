import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;


public class Miscelaneo extends BaseTest{

    @Test
    public void Test_HOME(){

        given()
                .get()
        .then()
                .body(containsString("Gin Boilerplate"))
                .statusCode(200);
    }

    @Test
    public void Test_PING(){

        given()
                .get( "/ping")
        .then()
                .body("response", equalTo("pong"))
                .statusCode(200);
    }

    @Test
    public void Test_404(){

        given()
                .get( "/asf")
                .then()
                //.body("response", equalTo("pong"))
                .statusCode(404);
    }
}
