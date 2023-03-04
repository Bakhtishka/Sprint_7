import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    Courier courier = new Courier();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        courier.createCourier();
    }

    @Test
    @DisplayName("Check if the courier can log in")
    public void checkCourierLogin() {
        String json = "{\"login\": \"cesar\", \"password\": \"1234\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(200)
                .assertThat().body("id", notNullValue());
    }

    @Test
    @DisplayName("For authorization, you need to pass all the required fields." +
            " This test is without the login and password fields.")
    public void checkCourierLoginWithOutPasswordField() {
        String json = "{\"login\": \"\", \"password\": \"\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("The system will return an error if the login is incorrect")
    public void shouldReturnErrorThanIncorrectLogin() {
        String json = "{\"login\": \"cesare\", \"password\": \"1234\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("The system will return an error if the password is incorrect")
    public void shouldReturnErrorThanIncorrectPassword() {
        String json = "{\"login\": \"cesar\", \"password\": \"1123\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("If login field is missing, the request returns an error")
    public void shouldReturnErrorThanMissingLoginField() {
        String json = "{\"login\": \"\", \"password\": \"1234\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("If password field is missing, the request returns an error")
    public void shouldReturnErrorThanMissingPasswordField() {
        String json = "{\"login\": \"cesar\", \"password\": \"\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("If you log in as a non-existent user, the request returns an error")
    public void shouldReturnErrorThanLoginNonExistentUser() {
        String json = "{\"login\": \"cleopatra\", \"password\": \"9999\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .statusCode(404)
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Successful request returns id")
    public void shouldReturnsIdThanLoginSuccessful() {
        String json = "{\"login\": \"cesar\", \"password\": \"1234\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login")
                .then()
                .assertThat().body("id", notNullValue());
    }

    @After //удаление курьера после каждого теста
    public void findCourierIdAndDelete() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\"}";
        Integer userId = given().
                header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier/login")
                .then()
                .extract()
                .path("id");
        if (userId != null) {
            given().delete("/api/v1/courier/" + userId);
        }
    }
}

