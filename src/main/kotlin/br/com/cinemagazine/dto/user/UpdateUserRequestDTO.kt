package br.com.cinemagazine.dto.user

import br.com.cinemagazine.constants.Gender
import jakarta.validation.constraints.NotBlank

data class UpdateUserRequestDTO(
    @NotBlank val firstName: String,
    @NotBlank val lastName: String,
    @NotBlank val email: String,
    @NotBlank val gender: Gender
)
