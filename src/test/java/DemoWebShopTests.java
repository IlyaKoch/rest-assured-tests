import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

public class DemoWebShopTests {

    @Test
    void addToCartTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .when()
                        .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .statusCode(200)
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        boolean message = jsonPath.get("success");
        assertThat(message).isEqualTo(true);
    }

    @Test
    void addToCartWithCookieTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .cookie("Nop.customer=d11846a2-4264-43e8-8726-f3a8590b37f4")
                        .when()
                        .post("https://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then().log().all()
                        .statusCode(200)
                        .extract().response();
        JsonPath jsonPath = response.jsonPath();
        boolean message = jsonPath.get("success");
        assertThat(message).isEqualTo(true);
    }

    @Test
    @DisplayName("Successful add item to cart to some demowebshop (API + UI)")
    void checkCountOfItemsInShoppingCart() {
        step("Get cookie by api and set it to browser", () -> {
            String authorizationCookie =
                    given()
                            .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                            .formParam("Email", "Kochetkov63@mail.com")
                            .formParam("Password", "12344321wow")
                            .when()
                            .post("https://demowebshop.tricentis.com/login")
                            .then().log().all()
                            .statusCode(302)
                            .extract()
                            .cookie("NOPCOMMERCE.AUTH");

            step("Open minimal content, because cookie can be set when site is opened", () ->
                    open("https://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png"));

            step("Set cookie to to browser", () ->
                    getWebDriver().manage().addCookie(
                            new Cookie("NOPCOMMERCE.AUTH", authorizationCookie)));
        });

        step("Open main page", () ->
                open("https://demowebshop.tricentis.com/"));

        step("Check count of items in shopping cart", () ->
                $x("//span[@class = 'cart-qty']").shouldHave(text("(2)")));
    }
}