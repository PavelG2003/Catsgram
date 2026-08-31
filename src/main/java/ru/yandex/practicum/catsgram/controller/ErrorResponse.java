package ru.yandex.practicum.catsgram.controller;

import lombok.Getter;

public class ErrorResponse {
    @Getter
    private String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

}
