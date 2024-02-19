package br.com.cinemagazine.dto.token

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestDTO(
    @field:NotBlank(message = "field token is required")
    val token: String?
)
