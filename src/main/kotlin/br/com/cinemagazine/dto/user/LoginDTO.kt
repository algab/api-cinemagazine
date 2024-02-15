package br.com.cinemagazine.dto.user

data class LoginDTO(
    val user: UserDTO,
    val accessToken: String,
    val refreshToken: String
)
