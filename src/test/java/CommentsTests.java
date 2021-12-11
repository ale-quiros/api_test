import helpers.DataHelper;
import helpers.PostHelper;
import model.Comment;
import org.hamcrest.core.StringContains;
import org.testng.annotations.Test;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentsTests extends BaseTest{

   // String postId = "7734";
    String resourcePath = "/v1/comment/";

    @Test
    public void Test_Create_Comment(){
        Comment newComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(newComment)
        .when()
                .post(resourcePath + PostHelper.getPostId())
        .then()
                .body("message", equalTo ("Comment created") )
                .statusCode(200);
    }

    @Test
    public void Test_Get_Posts(){
        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get(resourcePath+"s" + "/" + PostHelper.getPostId())
                .then()
                .body(StringContains.containsString("results"))
                .statusCode(200);
    }



}
