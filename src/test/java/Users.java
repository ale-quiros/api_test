import helpers.AuthHelper;
import helpers.DataHelper;
import model.User;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

import static org.hamcrest.core.StringContains.containsString;

public class Users extends BaseTest{

    @Test
    public void Test_User_Registration(){

        User newUser = new User(DataHelper.generateRandomName(),
                "pass",
                DataHelper.generateRandomEmail());

        // Preparación Headers/Body
        given()
                .body(newUser)
        // Ejecucion
        .when()
                .post("/v1/user/register")
        // Assertions / Verificaciones
        .then()
                .body("message", equalTo("Successfully registered"))
                .statusCode(200);
    }

    @Test
    public void Test_User_Registration_Existing_User(){

        // Existing user
        User existingUser = new User("Luis","pass", "luisramirez@mail.com");

        // Preparación Headers/Body
        given()
                .body(existingUser)
                // Ejecucion
                .when()
                .post("/v1/user/register")
                // Assertions / Verificaciones
                .then()
                .body("message", equalTo("User already exists"))
                .statusCode(406);
    }

    @Test
    public void Test_User_Login(){

        // Existing user
        User existingUser = new User("Luis","pablo", "pablo@test.com");

        // Preparación Headers/Body
        given()
                .body(existingUser)
                // Ejecucion
                .when()
                .post("/v1/user/login")
                // Assertions / Verificaciones
                .then()
                .body("message", equalTo("User signed in"))
                .statusCode(200);
    }

    @Test
    public void Test_User_Logout(){

        // Preparación Headers/Body
        given()
                .header("Authorization", String.format("Bearer %s", AuthHelper.getUserToken()))
                // Ejecucion
                .when()
                .get("/v1/user/logout")
                // Assertions / Verificaciones
                .then()
                .body("message", equalTo("Successfully logged out"))
                .statusCode(200);
    }
}
