import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    String firstName = "Аменхотеб";
    String lastName = "Амон";
    String address = "Египет, Каир";
    String phone = "+79999999999";
    int rentTime = 6;
    String deliveryDate = "01.08.2023";
    String comment = "Слава фараону!";
    private final String[] color;


    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    @Parameterized.Parameters
    public static Object[][] getColors() {
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Check that when you create an order:\n" +
            "you can specify one of the colors - BLACK or GRAY;")
    public void createOrderWithColors() {
        Response response = createOrder();
        checkOrderTrack(response);
        //  System.out.println(response.body().asString());
    }

    @Test
    @DisplayName("Check status kode when you create order")
    @Description("If status code equals 201, that response is successful")
    public void testStatusCode() {
        Response response = createOrder();
        checkStatusCodeWhenSuccessfulCreateOrder(response);
    }

    @Step("Send POST request to /api/v1/orders")
    public Response createOrder() {
        Order json = new Order(firstName, lastName, address, phone, rentTime, deliveryDate, comment, color);
        return given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/orders");
    }

    @Step("Check order track")
    public void checkOrderTrack(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }

    @Step("Check status code when successful create order")
    public void checkStatusCodeWhenSuccessfulCreateOrder(Response response) {
        response.then().statusCode(201);
    }
}

