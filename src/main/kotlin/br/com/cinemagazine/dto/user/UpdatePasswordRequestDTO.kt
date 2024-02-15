package br.com.cinemagazine.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdatePasswordRequestDTO(
    @field:NotBlank(message = "password field is required") @field:Size(min = 6, message = "minimum size of the password field is 6")
    val password: String?
)
