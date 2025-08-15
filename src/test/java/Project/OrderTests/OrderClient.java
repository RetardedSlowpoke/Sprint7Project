package Project.OrderTests;

import Project.Client;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderClient extends Client {

    @Step("Создаём заказ")
    public ValidatableResponse createOrder(OrderObj order) {
        return spec()
                .body(order)
                .when()
                .post("/orders")
                .then();
    }

    @Step("Отменяем заказ по track={track}")
    public ValidatableResponse cancelOrder(int track) {
        return spec()
                .queryParam("track", track)
                .when()
                .put("/orders/cancel")
                .then();
    }
}
