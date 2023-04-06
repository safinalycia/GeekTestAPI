package API.NotMyPosts;

import API.AbstractTest;
import API.service.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class NotMyPostTest extends AbstractTest {
    @Test
    @Tag("Positive")
    void getNotMyPostsAsc() {
        given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ASC)
                .queryParam(page, 5)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .spec(getResponseSpecificationPostsValid());
    }
    @Test
    @Tag("Positive")
    void getNotMyPostsAscPage15999() {
        given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ASC)
                .queryParam(page, 15999)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .spec(getResponseSpecificationPostsValid());
    }

    @Test
    @Tag("Positive")
    void getNotMyPostsDescPage1() {
        given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, DESC)
                .queryParam(page, 1)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .spec(getResponseSpecificationPostsValid());
    }
    @Test
    @Tag("Positive")
    void getNotMyPostsAllPage1() {
        given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ALL)
                .queryParam(page, 1)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .spec(getResponseSpecificationPostsValid());
    }

    @Test
    @Tag("Positive")
    void getNotMyPostsAllPage0() {
        given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ALL)
                .queryParam(page, 0)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .spec(getResponseSpecificationPostsValid());
    }

    @Test
    @Tag("Negative")
    void getNotMyPostsPageIsNegative() {
        JsonPath response = given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ASC)
                .queryParam(page, -1)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .statusCode(400)
                .extract().body().jsonPath();
        assertThat (response.getString("message"), equalTo("Bad request"));
    }

    @Test
    @Tag("Negative")
    void getNotMyPostsAscEmptyPage() {
        JsonPath response = given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order, ASC)
                .queryParam(page, 0)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .statusCode(400)
                .extract().body().jsonPath();
        assertThat (response.getString("message"), equalTo("Bad request"));
    }

    @Test
    @Tag("Negative")
    void getNotMyPostsEmptyOrderWithPageLetters() {
        JsonPath response = given()
                .spec(getRequestSpecificationNotMyPosts())
                .queryParam(owner,notMe)
                .queryParam(sort, createdAt)
                .queryParam(order)
                .queryParam(page, "Lorem")
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .statusCode(400)
                .extract().body().jsonPath();
        assertThat (response.getString("message"), equalTo("Bad request"));
    }

    @Test
    @Tag("Negative")
    void getMyPostsWithoutToken() {
        JsonPath response = given()
                .queryParam(sort, createdAt)
                .queryParam(order, DESC)
                .queryParam(page, 1)
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .statusCode(401)
                .extract().jsonPath();
        assertThat(response.getString("message"), equalTo("Auth header required X-Auth-Token"));
    }

    @Test
    @Tag("Negative")
    void getMyPostsInvalidToken() {
        JsonPath response = given()
                .header("X-Auth-Token", "4bdd557")
                .queryParam(sort, createdAt)
                .queryParam(order, ASC)
                .queryParam(page, 1)
                .contentType(ContentType.JSON)
                .when()
                .get(getBaseUrl() + Endpoints.getPosts)
                .then()
                .statusCode(401)
                .extract().jsonPath();
        assertThat(response.getString("message"), equalTo("No API token provided or is not valid"));
    }


}
