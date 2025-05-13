package ru.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import ru.praktikum.steps.CourierSteps;

import static org.hamcrest.Matchers.is;

public class CourierCreateTest {

    private CourierSteps courierSteps = new CourierSteps();
    private String login;
    private String password;
    private String firstName;

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Создание курьера со всеми заполненными валидными данными полями - возвращает 201 Created")
    public void createCourier() {
        String login = courierSteps.generateRandomLogin();
        String password = courierSteps.generateRandomPassword();
        String firstName = courierSteps.generateRandomFirstName();

        courierSteps
                .courierCreate(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        this.login = login;
        this.password = password;
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера без логина - возвращает 400 Bad Request")
    public void createCourierWithNullLogin() {
        password = courierSteps.generateRandomPassword();
        firstName = courierSteps.generateRandomFirstName();

        courierSteps
                .courierCreate(null, password, firstName)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с пустым значением в логине")
    @Description("Создание курьера с пустым значением в логине - возвращает 400 Bad Request")
    public void createCourierWithEmptyLogin() {
        password = courierSteps.generateRandomPassword();
        firstName = courierSteps.generateRandomFirstName();

        courierSteps
                .courierCreate("", password, firstName)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера без пароля - возвращает 400 Bad Request")
    public void createCourierWithNullPassword() {
        login = courierSteps.generateRandomLogin();
        firstName = courierSteps.generateRandomFirstName();

        courierSteps
                .courierCreate(login, null, firstName)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с пустым значением в пароле")
    @Description("Создание курьера с пустым значением в пароле - возвращает 400 Bad Request")
    public void createCourierWithEmptyPassword() {
        login = courierSteps.generateRandomLogin();
        firstName = courierSteps.generateRandomFirstName();

        courierSteps
                .courierCreate(login, "", firstName)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Создание курьера без имени - возвращает 400 Bad Request")
    public void createCourierWithNullFirstName() {
        login = courierSteps.generateRandomLogin();
        password = courierSteps.generateRandomPassword();

        courierSteps
                .courierCreate(login, password, null)
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени")
    @Description("Создание курьера без имени - возвращает 400 Bad Request")
    public void createCourierWithEmptyFirstName() {
        login = courierSteps.generateRandomLogin();
        password = courierSteps.generateRandomPassword();

        courierSteps
                .courierCreate(login, password, "")
                .statusCode(400)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание двух курьеров с одинаковыми данными")
    @Description("Создание двух курьеров с одинаковыми данными - возвращает 409 Сonflict")
    public void createTwoCouriersWithSameData() {
        String login = courierSteps.generateRandomLogin();
        String password = courierSteps.generateRandomPassword();
        String firstName = courierSteps.generateRandomFirstName();;

        courierSteps
                .courierCreate(login, password, firstName)
                .statusCode(201)
                .body("ok", is(true));

        this.login = login;
        this.password = password;

        courierSteps
                .courierCreate(login, password, firstName)
                .statusCode(409)
                .body("message", is("Этот логин уже используется"));
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
