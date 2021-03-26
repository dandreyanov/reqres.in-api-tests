package reqres.in;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.FileUtils.readStringFromFile;

public class Tests {

    @BeforeAll
    static void setup() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api/";
    }

    @Test
    void createUsersTest() {
        given()
                .contentType(JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"leader\" }")
                .when()
                .post("users")
                .then()
                .statusCode(201)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("leader"))
                .body("id", is(notNullValue()))
                .body("createdAt", is(notNullValue()));

    }

    @Test
    void updateUsersTest() {
        given()
                .contentType(JSON)
                .body("{ \"name\": \"morpheus\", \"job\": \"zion resident\" }")
                .when()
                .put("users/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("name", is("morpheus"))
                .body("job", is("zion resident"))
                .body("updatedAt", is(notNullValue()));

    }

    @Test
    void deleteUsersTest() {
        given()
                .when()
                .delete("users/2")
                .then()
                .statusCode(204)
        ;
    }

    @Test
    void notFoundTest() {
        given()
                .when()
                .get("unknown/23")
                .then()
                .statusCode(404)
        ;
    }

    @Test
    void getListUsers() {
        String listUser = readStringFromFile("./src/test/resources/ListUsersData.txt");
        given()
                .when()
                .get("users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .body(is(listUser))
        ;
    }


}
