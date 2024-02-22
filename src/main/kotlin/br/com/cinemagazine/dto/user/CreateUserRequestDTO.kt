package br.com.cinemagazine.dto.user

import br.com.cinemagazine.annotation.Values
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateUserRequestDTO(
    @field:NotBlank(message = "firstName field is required")
    val firstName: String?,
    @field:NotBlank(message = "lastName field is required")
    val lastName: String?,
    @field:NotBlank(message = "email field is required")
    @field:Email(message = "email is not valid")
    val email: String?,
    @field:NotBlank(message = "password field is required")
    @field:Size(min = 6, message = "minimum size of the password field is 6")
    val password: String?,
    @field:NotNull(message = "gender field is required")
    @field:Values(values = ["Masculine", "Feminine"], message = "gender field accept values: Masculine and Feminine")
    val gender: String?
)
