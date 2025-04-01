package ru.glebov.jwt.dto;

public record JwtRequest(String login, String password, String refreshToken, String[] roles) {
}
