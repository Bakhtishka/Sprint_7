import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        findCourierIdAndDelete();
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
    @Test
    @DisplayName("Check that it is possible to create a courier")
    @Description("If the response returns ok: true, then the courier has been created.")
    public void possibleCreateCourier() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(201)
                .assertThat().body("ok", equalTo(true));
    }

    @Test //тест падает, так как ОР не соответствует ФР
    @DisplayName("Cannot create two identical couriers")
    @Description("If you try to create two identical courier, an error will be returned." +
            "The test failed because the application has a bug: " +
            "The text in the response body does not match the expected result.")
    public void cannotCreateTwoIdenticalCouriers() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("To create a courier you need to pass all the required fields.")
    @Description("This test is without login.")
    public void testCreateCourierWithoutLogin() {
        String json = "{\"login\": \"\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("To create a courier you need to pass all the required fields.")
    @Description("This test is without password.")
    public void testCreateCourierWithoutPassword() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("The request returns the correct response code")
    public void theRequestReturnTheCorrectResponseCode() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("A successful request returns ok: true;")
    public void successfulRequestReturnsCorrectResponse() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("If one of the fields is missing, then returns an error.")
    @Description("This test is without login field.")
    public void requestWithoutLoginFieldReturnError() {
        String json = "{\"login\": \"\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("If one of the fields is missing, then returns an error.")
    @Description("This test is without password field.")
    public void requestWithoutPasswordFieldReturnError() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"\", \"firstName\": \"Amon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier")
                .then()
                .statusCode(400)
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("if you create a user with a login that already exists, an error is returned.")
    @Description("The test failed because the application has a bug:" +
            "The text in the response body does not match the expected result.")
    public void whenCreateUserWithAnExistingLoginReturnError() {
        String json = "{\"login\": \"amenkhoteb\", \"password\": \"7777\", \"firstName\": \"Amon\"}";
        String user = "{\"login\": \"amenkhoteb\", \"password\": \"8888\", \"firstName\": \"faraon\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
        given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/v1/courier")
                .then()
                .statusCode(409)
                .assertThat().body("message", equalTo("Этот логин уже используется"));
    }

}


