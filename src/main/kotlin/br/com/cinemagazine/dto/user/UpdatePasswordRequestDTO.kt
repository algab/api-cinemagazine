package br.com.cinemagazine.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatePasswordRequestDTO(@NotBlank @Size(min = 6) val password: String)
