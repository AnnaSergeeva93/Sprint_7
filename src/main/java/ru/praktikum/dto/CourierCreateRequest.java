package ru.praktikum.dto;

import lombok.Data;

@Data
public class CourierCreateRequest {
    private String login;
    private String password;
    private String firstName;
}
