package project.couriertests;

import project.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import project.objects.CourierObj;
import project.objects.Credentials;

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

    @Step("Удаляем курьера")
    public ValidatableResponse delete(int id) {
        return spec()
                .when()
                .delete("/courier/" + id)
                .then();
    }
}
