import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final String[] color;


    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
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
        CreateOrder json = new CreateOrder("Cesar", "Yuliy",
                "Rome, Cesar Palace", 1,
                "+19999999999", 5, "2023.03.21",
                "Si vis pacem, para bellum", color);
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/orders")
                .then()
                .assertThat().body("track", notNullValue());
    }
}

