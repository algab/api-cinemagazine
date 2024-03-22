package br.com.cinemagazine.constants

enum class ApiMessage(val description: String) {
    USER_NOT_FOUND("User not found"),
    EMAIL_ALREADY_EXISTS("Email already exists"),
    TOKEN_INVALID("Token invalid"),
    INVALID_MEDIA_TYPE("Invalid media type. Accepted values: movie and tv"),
    WATCHED_NOT_FOUND("Register not found"),
    WATCH_NOT_FOUND("Register not found"),
    REFRESH_TOKEN_NOT_FOUND("Register not found"),
    USER_UNAUTHORIZED("User without permission"),
    TOKEN_NOT_FOUND("Token not found");
}