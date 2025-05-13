package ru.praktikum.steps;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import ru.praktikum.constants.Url;
import ru.praktikum.dto.CourierCreateRequest;
import ru.praktikum.dto.CourierLoginRequest;

import static io.restassured.RestAssured.given;
import static ru.praktikum.constants.Url.*;

public class CourierSteps {

    static {
        RestAssured.config = RestAssured.config()
                .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails());
    }

    public static RequestSpecification spec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Url.MAIN_URL)
                .log()
                .all();
    }

    @Step("Генерация рандомного логина курьера")
    public String generateRandomLogin() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Генерация рандомного пароля курьера")
    public String generateRandomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Генерация рандомного имени курьера")
    public String generateRandomFirstName() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    @Step("Создание курьера")
    public ValidatableResponse courierCreate(String login, String password, String firstName) {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirstName(firstName);
        return spec()
                .body(courierCreateRequest)
                .post(COURIER_CREATE)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse courierLogin(String login, String password) {
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);
        return spec()
                .body(courierLoginRequest)
                .when()
                .post(COURIER_LOGIN)
                .then();
    }

    @Step("Получение id курьера")
    public int courierGetId(String login, String password) {
        return courierLogin(login, password)
                .extract()
                .response()
                .path("id");
    }

    @Step("Удаление курьера")
    public ValidatableResponse courierDelete(int id) {
        return spec()
                .pathParam("id", id)
                .when()
                .delete(COURIER_DELETE)
                .then();
    }
}
