import helpers.DataHelper;
import helpers.PostHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import model.Post;
import org.hamcrest.core.StringContains;
import org.testng.annotations.Test;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import specifications.ResponseSpecs;

public class CommentsTests extends BaseTest{

    String postId = "7734";
    String commentId = "1234";
    String resourcePath = "/v1/comment";

    @Test(priority=1)
    public void Test_Create_Comment(){
        postId = PostHelper.getPostId();
        Comment newComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(newComment)
        .when()
                .post(resourcePath + "/" +postId)
        .then()
                .body("message", equalTo ("Comment created") )
                .statusCode(200)
        .extract().
                response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        commentId = Integer.toString(jsonPathEvaluator.get("id"));

        System.out.println("test:" + commentId);
    }

    @Test (priority=2)
    public void Test_Get_Comment(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .body(StringContains.containsString("data"))
                .statusCode(200);
    }

    @Test (priority=3)
    public void Test_Get_Comments(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath+"s/" + postId)
        .then()
                .body(StringContains.containsString("results"))
                .statusCode(200);
    }

    @Test (priority=4)
    public void Test_Update_Post(){
        Comment updateComment = new Comment("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(updateComment)
        .when()
                .put(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Comment updated"))
                .statusCode(200);
    }

    @Test(priority=5)
    public void Test_Delete_Post(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .delete(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Comment deleted"))
                .statusCode(200);
    }



}
