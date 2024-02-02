package br.com.cinemagazine.dto.user

import br.com.cinemagazine.constants.Gender
import jakarta.validation.constraints.NotBlank

data class CreateUserRequestDTO(
    @NotBlank val firstName: String,
    @NotBlank val lastName: String,
    @NotBlank val email: String,
    @NotBlank val password: String,
    @NotBlank val gender: Gender
)
