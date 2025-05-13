package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;
import ru.praktikum.steps.OrderSteps;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class OrderGetListTest {
    private OrderSteps orderSteps = new OrderSteps();
    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Test
    @DisplayName("Получение списка заказов курьера")
    @Description("При получении списка заказов тело ответа содержит список")
    public void orderGetList() {
        orderSteps
                .orderGetList()
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение списка заказов курьера по его id")
    @Description("При получении списка заказов курьера по его id тело ответа содержит список")
    public void orderGetListWithCourierId() {
        login = courierSteps.generateRandomLogin();
        password = courierSteps.generateRandomPassword();
        firstName = courierSteps.generateRandomFirstName();
        courierSteps.courierCreate(login, password, firstName);

        int id = courierSteps.courierGetId(login, password);

        orderSteps
                .orderGetListWithCourierId(id)
                .statusCode(200)
                .body("orders", notNullValue());
    }

    @After
    public void tearDown() {
        if (login != null && password != null) {
            ValidatableResponse loginResponse = courierSteps.courierLogin(login, password)
                    .statusCode(200);
            int courierId = loginResponse.extract().path("id");
            courierSteps.courierDelete(courierId)
                    .statusCode(200)
                    .body("ok", Matchers.is(true));
        }
    }

}
