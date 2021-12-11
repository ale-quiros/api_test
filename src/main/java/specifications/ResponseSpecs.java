package specifications;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecs {

    // Access-Control-Allow-Origin: http://localhost

    public static ResponseSpecification defaultSpec(){

        //Metodos para bearer authentication
        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectHeader("Access-Control-Allow-Origin", "http://localhost");
        // Añadir Response Headers
        // Añadir Body
        return responseSpecBuilder.build();

        //Metodos para basic authentication
    }
}
