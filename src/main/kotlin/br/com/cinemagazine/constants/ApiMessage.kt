package br.com.cinemagazine.constants

enum class ApiMessage(val description: String) {
    USER_NOT_FOUND("User not found"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    TOKEN_INVALID("Token invalid");
}