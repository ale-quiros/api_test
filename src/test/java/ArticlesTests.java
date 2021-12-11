import helpers.AuthHelper;
import helpers.DataHelper;
import model.Article;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class ArticlesTests extends BaseTest{

    String resourcePath = "/v1/article";

    @Test
    public void Test_Create_Article(){

        Article newArticle = new Article(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        // Preparación Headers/Body
        given()
                .header("Authorization", String.format("Bearer %s", AuthHelper.getUserToken()))
                .body(newArticle)
                // Ejecucion
                .when()
                .post(resourcePath )
                // Assertions / Verificaciones
                .then()
                .body("message", equalTo("Article created"))
                .statusCode(200);
    }

    @Test
    public void Test_Create_Article_RequestSpecification(){

        Article newArticle = new Article(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        // Preparación Headers/Body
        given()
                .spec(RequestSpecs.generateToken())
                .body(newArticle)
                // Ejecucion
        .when()
                .post(resourcePath )
                // Assertions / Verificaciones
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Article created"))
                .statusCode(200);
    }
}
