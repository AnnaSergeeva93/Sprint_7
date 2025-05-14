package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.dto.OrderCreateRequest;


import static ru.praktikum.client.Client.spec;
import static ru.praktikum.constants.Url.*;

public class OrderSteps {


    @Step("Создание заказа")
    public ValidatableResponse orderCreate(OrderCreateRequest orderCreateRequest) {
        return spec()
                .body(orderCreateRequest)
                .post(ORDER_CREATE)
                .then();
    }

    @Step("Получение id заказа")
    public int orderGetId (ValidatableResponse response) {
        return response.extract().path("track");
    }

    @Step("Получение списка заказов курьера")
    public ValidatableResponse orderGetList() {
        return spec()
                .when()
                .get(ORDER_GET_LIST)
                .then();
    }

    @Step("Получение списка заказов курьера по его id")
    public ValidatableResponse orderGetListWithCourierId(int id) {
        return spec()
                .queryParam("id", id)
                .when()
                .get("/api/v1/orders")
                .then();
    }

    @Step("Завершение заказа")
    public ValidatableResponse orderCancel (int orderId) {
        return spec()
                .pathParam("id", orderId)
                .when()
                .put(ORDER_CANCEL)
                .then();
    }
}
