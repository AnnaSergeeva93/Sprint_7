package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    private CourierSteps courierSteps = new CourierSteps();
    private static String login = "laslo";
    private static String password = "123456";
    private static String firstName = "Kesha";

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Авторизация курьера свалидными данными - возвращает 200")
    public void loginCourier() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin(login, password)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Авторизация курьера без логина - возвращает 400 Bad Request")
    public void loginCourierWithoutLogin() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin("", password)
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Авторизация курьера без пароля - возвращает 400 Bad Request")
    public void loginCourierWithoutPassword() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin(login, "")
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без логина и без пароля")
    @Description("Авторизация курьера без логина и без пароля - возвращает 400 Bad Request")
    public void loginCourierWithoutLoginAndPassword() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin("", "")
                .statusCode(400)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным логином")
    @Description("Авторизация курьера с некорректным логином - возвращает 404 Not Found")
    public void loginCourierWithErrorLogin() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin("laslol", password)
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным паролем")
    @Description("Авторизация курьера с некорректным паролем - возвращает 404 Not Found")
    public void loginCourierWithErrorPassword() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin(login, "012345")
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректными логином и паролем")
    @Description("Авторизация курьера с некорректными логином и паролем - возвращает 404 Not Found")
    public void loginCourierWithErrorLoginAndPassword() {
        courierSteps
                .courierCreate(login, password, firstName);

        courierSteps
                .courierLogin("laslol", "012345")
                .statusCode(404)
                .body("message", is("Учетная запись не найдена"));
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
