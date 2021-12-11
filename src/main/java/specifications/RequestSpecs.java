package specifications;

import helpers.AuthHelper;
import helpers.DataHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.authentication.BasicAuthScheme;

public class RequestSpecs {

    public static RequestSpecification generateToken(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        String token = AuthHelper.getUserToken();
        // Add Header
        requestSpecBuilder.addHeader("Authorization", "Bearer "+token);

        return requestSpecBuilder.build();
    }

    public static RequestSpecification generateInvalidToken(){

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        String token = AuthHelper.getUserInvalidToken();
        // Add Header
        requestSpecBuilder.addHeader("Authorization", "Bearer "+token);

        return requestSpecBuilder.build();
    }

    public static RequestSpecification generateBasicAuth(){
        BasicAuthScheme basicAuth = new BasicAuthScheme();
        basicAuth.setUserName("testuser");
        basicAuth.setPassword("testpass");

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setAuth(basicAuth);
        return requestSpecBuilder.build();
    }

    public static RequestSpecification generateWrongBasicAuth(){
        BasicAuthScheme basicAuth = new BasicAuthScheme();
        basicAuth.setUserName("WrongUser");
        basicAuth.setPassword("WrongPass");

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setAuth(basicAuth);
        return requestSpecBuilder.build();
    }

}
