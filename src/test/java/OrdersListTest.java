import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check that a list of orders is returned in the response body.")
    public void checkReturnedListOfOrdersInTheResponseBody() {
        given()
                .get("/api/v1/orders")
                .then()
                .assertThat().body("orders", notNullValue());
    }
}