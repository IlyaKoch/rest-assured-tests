import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ReqresTests {

    private static final String TOKEN = "QpwL5tke4Pnpja7X4";

    @Test
    void successLoginTest() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "cityslicka");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("https://reqres.in/api/login")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String token = jsonPath.get("token");
        assertThat(token).isEqualTo(TOKEN);
    }

    @Test
    void unSuccessLoginTest() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("https://reqres.in/api/login")
                .then().log().all()
                .statusCode(400)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        assertThat(error).isEqualTo("Missing password");
    }

    @Test
    void successRegTest() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "pistol");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("https://reqres.in/api/register")
                .then().log().all()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");
        assertThat(token).isEqualTo(TOKEN);
        assertThat(id).isEqualTo(4);
    }

    @Test
    void unSuccessRegTest() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "sydney@fife");
        credentials.put("password", "");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentials)
                .when()
                .post("https://reqres.in/api/register")
                .then().log().all()
                .statusCode(400)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        assertThat(error).isEqualTo("Missing password");
    }

    @Test
    void deleteUserTest() {
        Response response = given()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then().log().all()
                .extract().response();
        assertThat(response.getStatusCode()).isEqualTo(204);
    }
}