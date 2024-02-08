package br.com.cinemagazine.dto.user

import br.com.cinemagazine.constants.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequestDTO(
    @NotBlank val firstName: String,
    @NotBlank val lastName: String,
    @NotBlank @Email val email: String,
    @NotBlank @Size(min = 6) val password: String,
    @NotBlank val gender: Gender
)
