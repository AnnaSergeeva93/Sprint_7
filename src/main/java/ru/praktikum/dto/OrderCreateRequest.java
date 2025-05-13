package ru.praktikum.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
    private String firstName = "Иван";
    private String lastName = "Иванов";
    private String address = "Москва, улица Кирова, 8";
    private int metroStation = 1;
    private String phone = "89876543210";
    private String rentTime = "1";
    private String deliveryDate = "2025-06-07";
    private String comment = "Привет";
    private List<String> color;

    public OrderCreateRequest (List<String> color) {
        this.color = color;
    }
}
