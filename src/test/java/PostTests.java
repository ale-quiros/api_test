import helpers.AuthHelper;
import helpers.DataHelper;
import helpers.PostHelper;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import model.Post;
import org.hamcrest.core.StringContains;
import org.testng.annotations.Test;
import specifications.RequestSpecs;
import specifications.ResponseSpecs;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

public class PostTests extends BaseTest{

    String postId = "";
    String resourcePath = "/v1/post";

    /*
    @Test
    public void Test_Create_Post(){

        Post newPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        // Preparación Headers/Body
        given()
                .header("Authorization", String.format("Bearer %s", AuthHelper.getUserToken()))
                .body(newPost)
                // Ejecucion
                .when()
                .post(resourcePath )
                // Assertions / Verificaciones
                .then()
                .body("message", equalTo("Post created"))
                .statusCode(200);
    }

     */


    /*---------------------------------------------------------------------------------------------------
    Crea un post con un titulo y contenido ramdom.  No tiene dependecias pero si usa autenticacion
    bearer entnoces recibe el token en el givem
    ----------------------------------------------------------------------------------------------------*/
    @Test
    public void Test_Create_Post_RequestSpecification(){
        Post newPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateToken())
                .body(newPost)
        .when()
                .post(resourcePath )
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post created"))
                .statusCode(200);
    }

    /*---------------------------------------------------------------------------------------------------
    Obtiene todos los posts. No tiene dependecias pero si usa autenticacion
    bearer entnoces recibe el token en el givem
    ----------------------------------------------------------------------------------------------------*/
    @Test
    public void Test_Get_Posts(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .get(resourcePath+"s")
        .then()
                .body(StringContains.containsString("results"))
                .statusCode(200);
    }

    /*---------------------------------------------------------------------------------------------------
       Retorna un post existente.
        Antes de correrlo debe existir un post entonces se llama la metodo PostHelper.getPostId() que crea
        un nuevo post y lo devuelve
     ----------------------------------------------------------------------------------------------------*/
    @Test
    public void Test_Get_Post(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .get(resourcePath + "/" + PostHelper.getPostId())
        .then()
                .body(StringContains.containsString("data"))
                .statusCode(200);
    }

    /*---------------------------------------------------------------------------------------------------
   Actualiza un post existente.
    Antes de correrlo debe existir un post entonces se llama la metodo PostHelper.getPostId() que crea
    un nuevo post y lo actualiza
 ----------------------------------------------------------------------------------------------------*/
    @Test
    public void Test_Update_Post(){
        Post updatePost = new Post("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateToken())
                .body(updatePost)
        .when()
                .put(resourcePath + "/" + PostHelper.getPostId())
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post updated"))
                .statusCode(200);
    }

    /*---------------------------------------------------------------------------------------------------
    Elimina un post existente.
    Antes de correrlo debe existir un post entonces se llama la metodo PostHelper.getPostId() que crea
    un nuevo post y lo actualiza
    ----------------------------------------------------------------------------------------------------*/
    @Test
    public void Test_Delete_Post(){
        Post updatePost = new Post("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateToken())
                .body(updatePost)
        .when()
                .delete(resourcePath + "/" + PostHelper.getPostId())
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post deleted"))
                .statusCode(200);
    }


}
