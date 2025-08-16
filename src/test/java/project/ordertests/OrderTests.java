package project.ordertests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import project.objects.OrderObj;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

class OrderTests {

    private OrderClient client;
    private Integer track;

    @BeforeEach
    void setUp() {
        client = new OrderClient();
        track = null;
    }



    static Stream<List<String>> colorCases() {
        return Stream.of(
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                List.of() // без цвета
        );
    }

    @ParameterizedTest(name = "Создание заказа: цвета = {0}")
    @MethodSource("colorCases")
    @DisplayName("Создание заказа с разными вариантами цвета (201, body содержит track)")
    @Description("Проверяем, что можно указать один, оба или ни одного цвета; успешный ответ возвращает track")
    void createOrder_withDifferentColors_returnsTrack(List<String> colors) {
        OrderObj order = new OrderObj(
                "Иван", "Иванов", "улица Пушкина, дом Колотушкина",
                "4",
                "88005553535",
                5,
                LocalDate.now().plusDays(1).toString(),
                "123",
                colors
        );

        ValidatableResponse resp = client.createOrder(order);
        track = resp
                .statusCode(201)
                .body("track", notNullValue())
                .extract().path("track");
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("GET /orders возвращает список заказов в теле ответа")
    public void getOrdersListTest() {
        ValidatableResponse response = client.spec()
                .when()
                .get("/orders")
                .then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders.size()", greaterThan(0));
    }
    @AfterEach
    void tearDown() {
        if (track != null) {
            client.cancelOrder(track)
                    .statusCode(anyOf(is(200), is(202), is(204)));
        }
    }
}