package ru.glebov.jwt.dto;

public record JwtResponse(int statusCode, String message, String token, String refreshToken) {

}

