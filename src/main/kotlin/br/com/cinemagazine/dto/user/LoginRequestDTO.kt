package br.com.cinemagazine.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequestDTO(
    @field:NotBlank(message = "email field is required") @field:Email(message = "email is not valid")
    val email: String?,
    @field:NotBlank(message = "password field is required") @field:Size(min = 6, message = "minimum size of the password field is 6")
    val password: String?
)
