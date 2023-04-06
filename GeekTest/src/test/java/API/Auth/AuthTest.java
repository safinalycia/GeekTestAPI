package API.Auth;


import API.AbstractTest;
import API.service.Endpoints;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;


public class AuthTest extends AbstractTest {


    @Test
    @Tag("Positive")
    void postAuthValidTest() {
        given()
                .spec(getRequestSpecificationValidUsernameAndPassword())
                .when()
                .post(getBaseUrl() + Endpoints.postAuth)
                .then()
                .spec(getResponseSpecificationAuthValid());
    }

    @Test
    @Tag("Negative")
    void postAuthInvalidUsernameTest() {
        given()
                .spec(getRequestSpecificationInvalidUsername())
                .when()
                .post(getBaseUrl() + Endpoints.postAuth)
                .then()
                .spec(getResponseSpecificationInvalidUsernameOrPassword());
    }

    @Test
    @Tag("Negative")
    void postAuthInvalidPasswordTest() {
        given()
                .spec(getRequestSpecificationInvalidPassword())
                .when()
                .post(getBaseUrl() + Endpoints.postAuth)
                .then()
                .spec(getResponseSpecificationInvalidUsernameOrPassword());
    }

}
