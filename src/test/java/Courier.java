import static io.restassured.RestAssured.given;

public class Courier {
    public Courier() {
    }

    public void createCourier() {
        String json = "{\"login\": \"cesar\", \"password\": \"1234\", \"firstName\": \"Yuliy\"}";
        given()
                .header("Content-type", "application/json")
                .body(json)
                .post("/api/v1/courier");
    }
}
