import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    @Test
    @DisplayName("Check that a list of orders is returned in the response body.")
    public void checkReturnedListOfOrdersInTheResponseBody() {
        Response response = returnListOrder();
        checkRequestBodyInSuccessfulResponse(response);
    }

    @Step("Send GET request to /api/v1/orders")
    public Response returnListOrder() {
        return given()
                .get("/api/v1/orders");
    }

    @Step("Check request body in successful response")
    public void checkRequestBodyInSuccessfulResponse(Response response) {
        response.then().assertThat().body("orders", notNullValue());
    }
}