package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;

import static org.apache.http.HttpStatus.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest {

    private CourierSteps courierSteps = new CourierSteps();
    private static String login = "laslo";
    private static String password = "123456";
    private static String firstName = "Kesha";

    @Before
    public void setUp() {
        courierSteps
                .courierCreate(login, password, firstName);
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Авторизация курьера свалидными данными - возвращает 200")
    public void loginCourier() {
        courierSteps
                .courierLogin(login, password)
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Авторизация курьера без логина - возвращает 400 Bad Request")
    public void loginCourierWithoutLogin() {
        courierSteps
                .courierLogin("", password)
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Авторизация курьера без пароля - возвращает 400 Bad Request")
    public void loginCourierWithoutPassword() {
        courierSteps
                .courierLogin(login, "")
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера без логина и без пароля")
    @Description("Авторизация курьера без логина и без пароля - возвращает 400 Bad Request")
    public void loginCourierWithoutLoginAndPassword() {
        courierSteps
                .courierLogin("", "")
                .statusCode(SC_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным логином")
    @Description("Авторизация курьера с некорректным логином - возвращает 404 Not Found")
    public void loginCourierWithErrorLogin() {
        courierSteps
                .courierLogin("laslol", password)
                .statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректным паролем")
    @Description("Авторизация курьера с некорректным паролем - возвращает 404 Not Found")
    public void loginCourierWithErrorPassword() {
        courierSteps
                .courierLogin(login, "012345")
                .statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация курьера с некорректными логином и паролем")
    @Description("Авторизация курьера с некорректными логином и паролем - возвращает 404 Not Found")
    public void loginCourierWithErrorLoginAndPassword() {
        courierSteps
                .courierLogin("laslol", "012345")
                .statusCode(SC_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }


    @After
    public void tearDown() {
        if (login != null && password != null) {
            ValidatableResponse loginResponse = courierSteps.courierLogin(login, password)
                    .statusCode(SC_OK);
            int courierId = loginResponse.extract().path("id");
            courierSteps.courierDelete(courierId)
                    .statusCode(SC_OK)
                    .body("ok", Matchers.is(true));
        }
    }

}
