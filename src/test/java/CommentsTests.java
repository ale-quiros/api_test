import helpers.DataHelper;
import helpers.PostHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Comment;
import org.hamcrest.core.StringContains;
import org.testng.annotations.Test;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import specifications.ResponseSpecs;

public class CommentsTests extends BaseTest{

    String postId = "";
    String commentId = "";
    String resourcePath = "/v1/comment";
//----------------------------------------------------------------------------------------------------------------------
    @Test(priority=1,dependsOnGroups="Posts")
    public void Positive_Test_Create_Comment(){
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
    }

    @Test(priority=1,dependsOnGroups="Posts")
    public void Negative_Test_Create_Comment_Missing_Param(){
        Comment newComment = new Comment(DataHelper.generateRandomName());

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(newComment)
        .when()
                .post(resourcePath + "/" +postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Invalid form"))
                .statusCode(406);
    }

    @Test(priority=1,dependsOnGroups="Posts")
    public void Negative_Test_Create_Comment_Security(){
        Comment newComment = new Comment(DataHelper.generateRandomName(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateWrongBasicAuth())
                .body(newComment)
        .when()
                .post(resourcePath + "/" +postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

    //----------------------------------------------------------------------------------------------------------------------

    @Test (priority=2,dependsOnGroups="Posts")
    public void Positive_Test_Get_Comment(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .body(StringContains.containsString("data"))
                .statusCode(200);
    }

    @Test (priority=2,dependsOnGroups="Posts")
    public void NegativeTest_Get_Comment_Wrong_PostID(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath + "/" + "-1" + "/" + commentId)
        .then()
                .statusCode(404);
    }

    @Test (priority=2,dependsOnGroups="Posts")
    public void Negative_Get_Comment_Security(){
        given()
                .spec(RequestSpecs.generateWrongBasicAuth())
        .when()
                .get(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }
    //----------------------------------------------------------------------------------------------------------------------


    @Test (priority=3,dependsOnGroups="Posts")
    public void Positive_Test_Get_Comments(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath+"s/" + postId)
        .then()
                .body(StringContains.containsString("results"))
                .statusCode(200);
    }

    @Test (priority=3,dependsOnGroups="Posts")
    public void Negative_Test_Get_Comments_Missing_PostID(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .get(resourcePath+"s/")
        .then()
                .statusCode(404);
    }

    @Test (priority=3,dependsOnGroups="Posts")
    public void Negative_Test_Get_Comments_Security(){
        given()
                .spec(RequestSpecs.generateWrongBasicAuth())
        .when()
                .get(resourcePath+"s/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

    //----------------------------------------------------------------------------------------------------------------------

    @Test (priority=4,dependsOnGroups="Posts")
    public void Positive_Test_Update_Post(){
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

    @Test (priority=4,dependsOnGroups="Posts")
    public void Negative_Test_Update_Post_Wrong_CommentID(){
        Comment updateComment = new Comment("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateBasicAuth())
                .body(updateComment)
        .when()
                .put(resourcePath + "/" + postId + "/" + "-100")
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Comment could not be updated"))
                .statusCode(406);
    }

    @Test (priority=4,dependsOnGroups="Posts")
    public void Negative_Test_Update_Post_Security(){
        Comment updateComment = new Comment("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateWrongBasicAuth())
                .body(updateComment)
        .when()
                .put(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }
    //----------------------------------------------------------------------------------------------------------------------

    @Test(priority=5,dependsOnGroups="Posts")
    public void Positive_Test_Delete_Post(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .delete(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Comment deleted"))
                .statusCode(200);
    }

    @Test(priority=5,dependsOnGroups="Posts")
    public void Negative_Test_Delete_Post_Wrong_URI(){
        given()
                .spec(RequestSpecs.generateBasicAuth())
        .when()
                .delete(resourcePath + "v1/" + postId + "/" + commentId)
        .then()
                .statusCode(404);
    }

    @Test(priority=5,dependsOnGroups="Posts")
    public void Negative_Test_Delete_Post_Security(){
        given()
                .spec(RequestSpecs.generateWrongBasicAuth())
        .when()
                .delete(resourcePath + "/" + postId + "/" + commentId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

}
