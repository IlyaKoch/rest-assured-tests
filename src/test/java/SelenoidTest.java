import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class SelenoidTest {

    @Test
    void checkTotal() {
        given().when().get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotalWithResponseAndBadPractice() {
        Response response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response();
        System.out.println(response.asString());
        System.out.println(response.path("total") + "");
        System.out.println(response.path("browsers") + "");
        System.out.println(response.path("browsers.firefox") + "");
    }

    @Test
    void checkTotalWithAssertJ() {
        Integer response =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(response);
        assertThat(response).isEqualTo(20);
    }

    @Test
    void checkSelenoidWdHubStatus200() {
        given()
                .auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}