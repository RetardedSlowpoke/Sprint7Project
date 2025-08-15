package Project.CourierTests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginTests {
    private CourierClient client;
    private CourierObj courier;
    private Integer courierId;

    @BeforeEach
    void setUp() {
        client = new CourierClient();
        courier = CourierObj.random();
        courierId = null;
    }


    @Test
    @DisplayName("Курьер может залогиниться")
    @Description("Проверяем возможность создать курьера. Потом удаляем.")

void courierCanLogin(){ //Пункты "может авторизоваться", "успешный запрос возвращает id" (мы же используем его для удаления всё же).

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

        static Stream<Credentials> missingFields() {

            Credentials noLogin = new Credentials(null, "pass1234");
            Credentials noPassword = new Credentials("login_" + (int)(Math.random() * 90000 + 10000), null);
            return Stream.of(noLogin, noPassword);
        }

        @ParameterizedTest
        @MethodSource("missingFields")
        @DisplayName("Авторизация: отсутствие обязательного поля возвращает 400") //Запрос без пароля возвращает 504 вместо 400. Очевидно, баг, проверил в Postman руками - всё тоже самое.
        @Description("При отсутствии логина или пароля возвращается 400 и сообщение об ошибке")
        void courierCannotLoginWithoutAllFieldsFilled(Credentials creds) {
            client.logIn(creds)
                    .statusCode(400)
                    .body("message", equalTo("Недостаточно данных для входа"));
        }
    @Test
    @DisplayName("Авторизация под несуществующим пользователем возвращает ошибку")
    @Description("POST /courier/login с данными несуществующего курьера возвращает 404 и сообщение об ошибке")
    public void loginWithNonExistentCourierShouldReturnError() {

        String fakeLogin = "nonexist_" + ThreadLocalRandom.current().nextInt(10000, 99999);
        String fakePassword = "pass_" + ThreadLocalRandom.current().nextInt(10000, 99999);
        Credentials creds = new Credentials(fakeLogin, fakePassword);


        ValidatableResponse loginResponse = client.logIn(creds);


        loginResponse
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            client.delete(courierId);
        }
    }
    }


