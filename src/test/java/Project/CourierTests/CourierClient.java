package Project.CourierTests;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import Project.Client;
import java.util.Map;
public class CourierClient extends Client {

        @Step("Логин")
        public ValidatableResponse logIn(Credentials creds) {
            return spec()
                    .body(creds)
                    .when()
                    .post("/courier/login")
                    .then().log().all();
        }

        @Step("Создание курьера")
        public ValidatableResponse createCourier(CourierObj courier) {
            return spec()
                    .body(courier)
                    .when()
                    .post("/courier")
                    .then().log().all();
        }

        @Step("Удаление курьера")
        public ValidatableResponse delete(int id) {
            return spec()
                    .body(Map.of("id", id))
                    .when()
                    .delete("/courier/" + id)
                    .then().log().all();
        }
    }

