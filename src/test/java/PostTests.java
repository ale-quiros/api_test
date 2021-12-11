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
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;


public class PostTests extends BaseTest{

    String postId = ""; // Variable global que sera seteada por Test_Create_Post_RequestSpecification y utilizada en varios metodos
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

    /*------------------------------------------------------------------------------------------------------
    Test Positivo de create Post:
    - Necesita un token bearer porque necesita estar logeado
    - Crea un post
    - Valida un mensaje de Post Created y el status 200
    - Setea una variable  postId con el ID del post recien creado el que es utilizado por los otros metodos
      por lo que los otros metodos dependen de este en especifico
     -------------------------------------------------------------------------------------------------------*/

    @Test(priority=1 ,groups="Posts")
    public void Positive_Test_Create_Post(){
        Post newPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        Response response = given()
                .spec(RequestSpecs.generateToken())
                .body(newPost)
        .when()
                .post(resourcePath )
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post created"))
                .statusCode(200)
        .extract().response();

        JsonPath jsonPathEvaluator = response.jsonPath();
        postId = Integer.toString(jsonPathEvaluator.get("id"));
    }

    /*------------------------------------------------------------------------------------------------------
    Test Negativo de create Post:
    - Necesita un token bearer porque necesita estar logeado
    - No recibe Json con la info para crear un post
    - Valida un mensaje de Invalid form y el status 406
 -------------------------------------------------------------------------------------------------------*/

    @Test(priority=1 ,groups="Posts")
    public void Negative_Test_CreatePost_No_Params (){
       String emptyJson = "";

        given()
                .spec(RequestSpecs.generateToken())
                .body(emptyJson)
        .when()
                .post(resourcePath )
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Invalid form"))
                .statusCode(406);
    }

/*------------------------------------------------------------------------------------------------------
    Test Seguridad Negativo de create Post:
    - Necesita un token bearer porque necesita estar logeado
    - No recibe Json con la info para crear un post
    - Valida un mensaje de Invalid form y el status 406
 -------------------------------------------------------------------------------------------------------*/

    @Test(priority=1 ,groups="Posts")
    public void Negative_Test_CreatePost_Security (){
        Post newPost = new Post(DataHelper.generateRandomTitle(), DataHelper.generateRandomContent());

        given()
                .spec(RequestSpecs.generateInvalidToken())
                .body(newPost)
        .when()
                .post(resourcePath )
       .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

/*------------------------------------------------------------------------------------------------------
    Test Positivo de get Post:
    - Necesita un token bearer porque necesita estar logeado
    - Depende de Positive_Test_Create_Post porque utiliza un postId que es seteada por este metodo
    - Valida un mensaje que el response contenga un String data y el status 200
 -------------------------------------------------------------------------------------------------------*/

    @Test (priority=2,groups="Posts")
    public void Positive_Test_Get_Post(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .get(resourcePath + "/" + postId)
        .then()
                .body(StringContains.containsString("data"))
                .statusCode(200);
    }

    /*------------------------------------------------------------------------------------------------------
    Test Positivo de get Post:
    - Necesita un token bearer porque necesita estar logeado
    - Depende de Positive_Test_Create_Post porque utiliza un postId que es seteada por este metodo
    - Valida un status 404 puesto que la pagina no existiria
 -------------------------------------------------------------------------------------------------------*/

    @Test (priority=2,groups="Posts")
    public void Negative_Test_Get_Post_WrongPostID(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .get(resourcePath + "/" + "WrongPostID")
        .then()
                .statusCode(404);
    }

    /*------------------------------------------------------------------------------------------------------
        Test Seguridad Negativo de Seguridad:
        - Necesita un token bearer porque necesita estar logeado
        - No recibe Json con la info para crear un post
        - Valida un mensaje de Logging first y el status 401
     -------------------------------------------------------------------------------------------------------*/

    @Test (priority=2,groups="Posts")
    public void Negative_Test_Get_Post_Security(){
        given()
                .spec(RequestSpecs.generateInvalidToken())
        .when()
                .get(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

    /*------------------------------------------------------------------------------------------------------
    Test Positivo de update Post:
    - Necesita un token bearer porque necesita estar logeado
    - Depende de Positive_Test_Create_Post porque utiliza un postId que es seteada por este metodo
    - Valida un mensaje que el response contenga un Post updated y el status 200
 -------------------------------------------------------------------------------------------------------*/

    @Test (priority=3,groups="Posts")
    public void Positive_Test_Update_Post(){
        Post updatePost = new Post("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateToken())
                .body(updatePost)
        .when()
                .put(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post updated"))
                .statusCode(200);
    }
    /*------------------------------------------------------------------------------------------------------
    Test Negativo de update Post:
    - Necesita un token bearer porque necesita estar logeado
    - Para fallar solo recibe un parametro en vez de los dos requeridos
    - Valida un mensaje que el response contenga un invalid form y el status 406
 -------------------------------------------------------------------------------------------------------*/

    @Test (priority=3,groups="Posts")
    public void Negative_Test_Update_Post_Missing_Param(){
        Post updatePost = new Post("Title updated");

        given()
                .spec(RequestSpecs.generateToken())
                .body(updatePost)
        .when()
                .put(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Invalid form"))
                .statusCode(406);
    }

     /*------------------------------------------------------------------------------------------------------
        Test Seguridad Negativo de Seguridad:
        - Necesita un token bearer porque necesita estar logeado
        - No recibe Json con la info para crear un post
        - Valida un mensaje de Logging first y el status 401
     -------------------------------------------------------------------------------------------------------*/

    @Test (priority=3,groups="Posts")
    public void Negative_Test_Update_Post_Security(){
        Post updatePost = new Post("Title updated","Content Updated");

        given()
                .spec(RequestSpecs.generateInvalidToken())
                .body(updatePost)
        .when()
                .put(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

    /*------------------------------------------------------------------------------------------------------
    Test Positivo de get Posts:
    - Necesita un token bearer porque necesita estar logeado
    - Valida un mensaje que el response contenga un Results y el status 200
 -------------------------------------------------------------------------------------------------------*/

    @Test(priority=4,groups="Posts")
    public void Positive_Test_Get_Posts(){
        given()
                .spec(RequestSpecs.generateToken())
                .when()
                .get(resourcePath+"s")
                .then()
                .body(StringContains.containsString("results"))
                .statusCode(200);
    }

    /*------------------------------------------------------------------------------------------------------
      Test Negativo de update Post:
      - Necesita un token bearer porque necesita estar logeado
      - Para fallar en el uri se especifica post en vez de posts para hacerlo incorrecto
      - Valida un status 404 puesto que el URI no existe
   -------------------------------------------------------------------------------------------------------*/

    @Test(priority=4,groups="Posts")
    public void Negative_Test_Get_Posts_WrongURI(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .get(resourcePath)
        .then()
                .statusCode(404);
    }

    /*------------------------------------------------------------------------------------------------------
    Test Negativo de Seguridad de get Posts:
    - Valida un mensaje de Logging first y el status 401
 -------------------------------------------------------------------------------------------------------*/

    @Test(priority=4,groups="Posts")
    public void Negative_Test_Get_Posts(){
        given()
                .spec(RequestSpecs.generateInvalidToken())
        .when()
                .get(resourcePath+"s")
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }

    /*------------------------------------------------------------------------------------------------------
Test Positivo de delete Post:
- Necesita un token bearer porque necesita estar logeado
- Depende de Positive_Test_Create_Post porque utiliza un postId que es seteada por este metodo
- Valida un mensaje que el response contenga un Post deleted y el status 200
-------------------------------------------------------------------------------------------------------*/

    @Test(priority=5,groups="Posts")
    public void Positive_Test_Delete_Post(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .delete(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Post deleted"))
                .statusCode(200);
    }

    /*------------------------------------------------------------------------------------------------------
      Test Negativo de update Post:
      - Necesita un token bearer porque necesita estar logeado
      - Para fallar en el uri se especifica un PostID que no existe
      - Valida un status 404 puesto que el URI no existe
   -------------------------------------------------------------------------------------------------------*/


    @Test(priority=5,groups="Posts")
    public void Negative_Test_Delete_Post_Worng_PostID(){
        given()
                .spec(RequestSpecs.generateToken())
        .when()
                .delete(resourcePath + "/" + "Wrong")
        .then()
                .statusCode(404);
    }

        /*------------------------------------------------------------------------------------------------------
    Test Negativo de Seguridad de get Posts:
    - Valida un mensaje de Logging first y el status 401
 -------------------------------------------------------------------------------------------------------*/

    @Test(priority=5,groups="Posts")
    public void Negative_Test_Delete_Post_Security(){
        given()
                .spec(RequestSpecs.generateInvalidToken())
        .when()
                .delete(resourcePath + "/" + postId)
        .then()
                .spec(ResponseSpecs.defaultSpec())
                .body("message", equalTo("Please login first"))
                .statusCode(401);
    }
}
