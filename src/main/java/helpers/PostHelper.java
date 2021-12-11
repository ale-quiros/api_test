package helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Post;
import model.User;
import specifications.RequestSpecs;

import static io.restassured.RestAssured.given;

public class PostHelper {

    static String resourcePath = "/v1/post";

    public static String getPostId(){
        Post newPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        // Preparación Headers/Body
        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(newPost)
                .when()
                .post(resourcePath);

        JsonPath jsonPathEvaluator = response.jsonPath();
        String postID = Integer.toString(jsonPathEvaluator.get("id"));

        System.out.println("postID: " + postID);

        return postID;
    }

    public static String getUserInvalidToken(){
        return "invalid";
    }
}
