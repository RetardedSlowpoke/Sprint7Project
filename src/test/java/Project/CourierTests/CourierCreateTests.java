package Project.CourierTests;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

class CourierCreateTests {

    private CourierClient client;
    private CourierObj courier;
    private Integer courierId;

    @BeforeEach
    void setUp() {
        client = new CourierClient();
        courier = CourierObj.random();
        courierId = null;
    }


    //Кажется пункты в задании дублируются, а ответы мы и так проверяем.
    @Test
    @DisplayName("Курьера можно создать (201)") //Пункты "курьера можно создать; успешный запрос возвращает ok: true;"
    @Description("POST /courier возвращает 201 и ok:true; затем логинимся, получаем id и удаляем курьера")
    void courierCanBeCreated() {

        ValidatableResponse createResponse = client.createCourier(courier);
        createResponse
                .statusCode(201)
                .body("ok", equalTo(true));

        ValidatableResponse loginResponse = client.logIn(Credentials.from(courier));
        courierId = loginResponse
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров (409)") //Пункты "нельзя создать двух одинаковых курьеров; если создать пользователя с логином, который уже есть, возвращается ошибка."
    @Description("Сначала успешно создаём курьера, затем повторяем создание с тем же логином и получаем 409")
    void cannotCreateTwoIdenticalCouriers() {
        ValidatableResponse firstCreate = client.createCourier(courier);
        firstCreate
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = client.logIn(Credentials.from(courier))
                .statusCode(200)
                .body("id", notNullValue())
                .extract().path("id");

        client.createCourier(courier)
                .statusCode(409).body("message",equalTo("Этот логин уже используется. Попробуйте другой."));
    }
    static Stream<CourierObj> courierWithMissingField() {
        int rndLogin = ThreadLocalRandom.current().nextInt(10000, 99999);
        return Stream.of(
                new CourierObj(null, "pass1234", "Ivan"),
                new CourierObj("login_" + rndLogin, null, "Ivan")
        );
    }

    @ParameterizedTest
    @MethodSource("courierWithMissingField")
    @DisplayName("Создание курьера без обязательного поля (400)")
    @Description("Проверка, что при отсутствии обязательного поля API возвращает 400 и сообщение об ошибке")
    void cannotCreateWithoutRequiredField(CourierObj courier) {
        client.createCourier(courier)
                .statusCode(400)
                .body("message",equalTo("Недостаточно данных для создания учетной записи"));
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            client.delete(courierId);
        }
    }
}


