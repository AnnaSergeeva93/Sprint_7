package ru.praktikum.dto;

import lombok.Data;

@Data
public class CourierLoginRequest {
    private String login;
    private String password;
}
