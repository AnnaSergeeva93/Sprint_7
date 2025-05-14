package ru.praktikum;


import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.dto.OrderCreateRequest;
import ru.praktikum.steps.OrderSteps;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreateTest {

    private OrderSteps orderSteps = new OrderSteps();
    private List<String> color;
    private int orderId;

    public OrderCreateTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters (name = "Цвет самоката - {0}")
    public static Object[][] dataGen() {
        return new Object[][] {
                {List.of("BLACK", "GREY")},
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of()}
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа с самокатами разных цветов - возвращает 201 Created")
    public void orderCreate() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(color);

        ValidatableResponse response = orderSteps.orderCreate(orderCreateRequest);

        response
                .statusCode(SC_CREATED)
                .body("track", notNullValue());

        orderId = orderSteps.orderGetId(response);
    }

    @After
    public void tearDown() {
        if (orderId != 0) {
            orderSteps.orderCancel(orderId);
        }
    }
}
