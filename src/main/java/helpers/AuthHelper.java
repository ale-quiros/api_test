package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.User;
import static io.restassured.RestAssured.given;

public class AuthHelper {

    public static String getUserToken(){
        // Existing user
        User existingUser = new User("Luis","pablo", "pablo@test.com");

        // Preparación Headers/Body
        Response response = given()
                .body(existingUser)
                // Ejecucion
                .when()
                .post("/v1/user/login");


        JsonPath jsonPathEvaluator = response.jsonPath();
        String token = jsonPathEvaluator.get("token.access_token");

        System.out.println("Token: " + token);

        return token;
    }

    public static String getUserInvalidToken(){
        return "invalid";
    }
}
