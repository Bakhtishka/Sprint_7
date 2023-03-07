import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    Faker faker = new Faker();
    String login = faker.name().firstName();
    String password = faker.internet().password();
    String lastName = faker.name().lastName();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
        findCourierIdAndDelete();
    }

    @After //удаление курьера после каждого теста
    public void findCourierIdAndDelete() {
        Courier json = new Courier(login, password, lastName);
        Integer userId = given()
                .header("Content-type", "application/json")
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
        Response response = createCourier();
        checkRequestBodyInSuccessfulResponse(response);
        checkStatusCodeInSuccessfulResponse(response);
    }

    @Test //тест падает, так как ОР не соответствует ФР
    @DisplayName("Cannot create two identical couriers")
    @Description("If you try to create two identical courier, an error will be returned." +
            "The test failed because the application has a bug: " +
            "The text in the response body does not match the expected result.")
    public void cannotCreateTwoIdenticalCouriersAndCheckRequest() {
        createCourier();
        Response response = createCourier();
        checkRequestBodyErrorText(response, "Этот логин уже используется");
        checkStatusCodWhenCannotCreateTwoIdenticalCouriers(response);
    }


    @Test
    @DisplayName("To create a courier you need to pass all the required fields.")
    @Description("This test is without login.")
    public void testCreateCourierWithoutLogin() {
        Response response = requestWithoutLogin();
        responseWithoutLoginReturnErrorText(response, "Недостаточно данных для создания учетной записи");
        requestWithoutLoginReturnStatusCode400(response);

    }

    @Test
    @DisplayName("To create a courier you need to pass all the required fields.")
    @Description("This test is without password.")
    public void testCreateCourierWithoutPassword() {
        Response response = requestWithoutPassword();
        responseWithoutPasswordReturnErrorText(response, "Недостаточно данных для создания учетной записи");
        requestWithoutPasswordReturnStatusCode400(response);
    }

    @Test
    @DisplayName("The request returns the correct response code")
    public void theRequestReturnTheCorrectResponseCode() {
        Response response = createCourier();
        checkStatusCodeInSuccessfulResponse(response);
    }

    @Test
    @DisplayName("A successful request returns ok: true;")
    public void successfulRequestReturnsCorrectResponse() {
        Response response = createCourier();
        checkRequestBodyInSuccessfulResponse(response);
    }

    @Test
    @DisplayName("If one of the fields is missing, then returns an error.")
    @Description("This test is without login field.")
    public void requestWithoutLoginFieldReturnError() {
        Response response = requestWithoutLogin();
        checkRequestBodyErrorText(response, "Недостаточно данных для создания учетной записи");
        requestWithoutPasswordReturnStatusCode400(response);
    }

    @Test
    @DisplayName("If one of the fields is missing, then returns an error.")
    @Description("This test is without password field.")
    public void requestWithoutPasswordFieldReturnError() {
        Response response = requestWithoutPassword();
        responseWithoutPasswordReturnErrorText(response, "Недостаточно данных для создания учетной записи");
        requestWithoutPasswordReturnStatusCode400(response);
    }

    @Test
    @DisplayName("if you create a user with a login that already exists, an error is returned.")
    @Description("The test failed because the application has a bug:" +
            "The text in the response body does not match the expected result.")
    public void whenCreateUserWithAnExistingLoginReturnError() {
        createCourier();
        Response response1 = createCourierWithLoginThatAlreadyExist();
        checkStatusCodWhenCreateCourierWithAlreadyExistLogin(response1);
        checkRequestBodyReturnedErrorText(response1, "Этот логин уже используется");
    }


    @Step("Send POST request to /api/v1/courier")
    public Response createCourier() {
        Courier json = new Courier(login, password, lastName);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Step("Check request body in successful response")
    public void checkRequestBodyInSuccessfulResponse(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }

    @Step("Check status code in successful response")
    public void checkStatusCodeInSuccessfulResponse(Response response) {
        response.then().statusCode(201);
    }

    @Step("Compare request body text with expected text")
    public void checkRequestBodyErrorText(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Step("Check status code when cannot create two identical couriers")
    public void checkStatusCodWhenCannotCreateTwoIdenticalCouriers(Response response) {
        response.then().statusCode(409);
    }

    @Step("response without login return statusCode 400")
    public void requestWithoutLoginReturnStatusCode400(Response response) {
        response.then().statusCode(400);
    }

    @Step("response without login return error text")
    public void responseWithoutLoginReturnErrorText(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("response without password return statusCode 400")
    public void requestWithoutPasswordReturnStatusCode400(Response response) {
        response.then().statusCode(400);
    }

    @Step("response without login return error text")
    public void responseWithoutPasswordReturnErrorText(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Response without login")
    public Response requestWithoutLogin() {
        Courier json = new Courier("", password, lastName);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Step("Response without login")
    public Response requestWithoutPassword() {
        Courier json = new Courier(login, "", lastName);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }

    @Step("create a user with a login that already exists, return error text")
    public void checkRequestBodyReturnedErrorText(Response response, String ignoredText) {
        response.then().assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Step("create courier with a login that already exist return status code 409")
    public void checkStatusCodWhenCreateCourierWithAlreadyExistLogin(Response response) {
        response.then().statusCode(409);
    }

    @Step("Send POST request to /api/v1/courier")
    public Response createCourierWithLoginThatAlreadyExist() {
        Courier json = new Courier(login, password, lastName);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }
}


