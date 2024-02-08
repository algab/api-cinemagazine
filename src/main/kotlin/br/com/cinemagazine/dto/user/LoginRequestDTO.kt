package br.com.cinemagazine.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequestDTO(@NotBlank @Email val email: String, @NotBlank @Size(min = 6) val password: String)
