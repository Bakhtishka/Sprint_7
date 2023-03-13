import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    Faker faker = new Faker();
    String login = faker.name().firstName();
    String password = faker.numerify("1234");

    public void createCourier() {
        Courier json = new Courier(login, password);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        createCourier();
    }


    @Step("Send POST request to api/v1/courier/login")
    public Response courierLogin() {
        Courier json = new Courier(login, password);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }



    @Test
    @DisplayName("Check if the courier can log in")
    public void checkCourierLogin() {
        Response response = courierLogin();
        checkStatusCodeInSuccessfulResponse(response);
        checkRequestBodyInSuccessfulResponse(response);

    }

    @Step("Login courier without login and password")
    public Response courierLoginWithoutRequiredFields() {
        Courier json = new Courier("", "");
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorText(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Check status code when login without required fields")
    public void checkStatusCodWhenLoginWithoutRequiredFields(Response response) {
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("For authorization, you need to pass all the required fields." +
            " This test is without the login and password fields.")
    public void checkCourierLoginWithOutRequiredFields() {
        Response response = courierLoginWithoutRequiredFields();
        checkStatusCodWhenLoginWithoutRequiredFields(response);
        checkRequestBodyErrorText(response, "Недостаточно данных для входа");
    }

    @Step("Login courier incorrect login field")
    public Response courierLoginIncorrectLoginField() {
        Courier json = new Courier(login + "e", password);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorIncorrectLoginField(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Check status code when input incorrect login field")
    public void checkStatusCodWhenInputIncorrectLoginField(Response response) {
        response.then().statusCode(404);
    }

    @Test
    @DisplayName("The system will return an error if the login is incorrect")
    public void shouldReturnErrorThanIncorrectLogin() {
        Response response = courierLoginIncorrectLoginField();
        checkRequestBodyErrorIncorrectLoginField(response, "Учетная запись не найдена");
        checkStatusCodWhenInputIncorrectLoginField(response);
    }

    @Step("Login courier incorrect password field")
    public Response courierLoginIncorrectPasswordField() {
        Courier json = new Courier(login, password + "1");
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorIncorrectLoginPasswordField(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Check status code when input incorrect password field")
    public void checkStatusCodWhenInputIncorrectPasswordField(Response response) {
        response.then().statusCode(404);
    }


    @Test
    @DisplayName("The system will return an error if the password is incorrect")
    public void shouldReturnErrorThanIncorrectPassword() {
        Response response = courierLoginIncorrectPasswordField();
        checkRequestBodyErrorIncorrectLoginPasswordField(response, "Учетная запись не найдена");
        checkStatusCodWhenInputIncorrectPasswordField(response);
    }

    @Step("Login courier with missed login field")
    public Response courierLoginWithoutLoginField() {
        Courier json = new Courier(null, password);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorWithoutLoginField(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Check status code when input incorrect password field")
    public void checkStatusCodWithoutLoginField(Response response) {
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("If login field is missing, the request returns an error")
    public void shouldReturnErrorThanMissingLoginField() {
        Response response = courierLoginWithoutLoginField();
        checkRequestBodyErrorWithoutLoginField(response, "Недостаточно данных для входа");
        checkStatusCodWithoutLoginField(response);
    }

    @Step("Login courier with missed password field")
    public Response courierLoginWithoutPasswordField() {
        Courier json = new Courier(login, "");
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorWithoutPasswordField(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Check status code when missing password field")
    public void checkStatusCodWithoutPasswordField(Response response) {
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("If password field is missing, the request returns an error")
    public void shouldReturnErrorThanMissingPasswordField() {
        Response response = courierLoginWithoutPasswordField();
        checkStatusCodWithoutPasswordField(response);
        checkRequestBodyErrorWithoutPasswordField(response, "Недостаточно данных для входа");

    }

    @Step("Login courier with non-existent user")
    public Response courierLoginWithNon_existentUser() {
        Courier json = new Courier(login + "aaa", password);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("api/v1/courier/login");
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorWithNon_existentUser(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Check status code when missing password field")
    public void checkStatusCodWithNon_existentUser(Response response) {
        response.then().statusCode(404);
    }

    @Test
    @DisplayName("If you log in as a non-existent user, the request returns an error")
    public void shouldReturnErrorThanLoginNonExistentUser() {
        Response response = courierLoginWithNon_existentUser();
        checkRequestBodyErrorWithNon_existentUser(response, "Учетная запись не найдена");
        checkStatusCodWithNon_existentUser(response);
    }

    @Test
    @DisplayName("Successful request returns id")
    public void shouldReturnsIdThanLoginSuccessful() {
        Response response = courierLogin();
        checkRequestBodyInSuccessfulResponse(response);
    }
    @Step("Check request body in successful response")
    public void checkRequestBodyInSuccessfulResponse(Response response) {
        response.then().assertThat().body("id", notNullValue());
    }

    @Step("Check status code in successful response")
    public void checkStatusCodeInSuccessfulResponse(Response response) {
        response.then().statusCode(200);
    }

    @After //удаление курьера после каждого теста
    public void findCourierIdAndDelete() {
        Courier json = new Courier(login, password);
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

