package Project.CourierTests;

import Project.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
public class CourierClient extends Client {

    @Step("Создаём курьера")
    public ValidatableResponse createCourier(CourierObj courier) {
        return spec()
                .body(courier)
                .when()
                .post("/courier")
                .then();
    }

    @Step("Логинимся курьером")
    public ValidatableResponse logIn(Credentials creds) {
        return spec()
                .body(creds)
                .when()
                .post("/courier/login")
                .then();
    }

    @Step("Удаляем курьера {id}")
    public ValidatableResponse delete(int id) {
        return spec()
                .when()
                .delete("/courier/" + id)
                .then();
    }
}
