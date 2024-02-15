package br.com.cinemagazine.dto.user

import br.com.cinemagazine.annotation.ListValues
import br.com.cinemagazine.constants.Gender
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateUserRequestDTO(
    @field:NotBlank(message = "firstName field is required")
    val firstName: String?,
    @field:NotBlank(message = "lastName field is required")
    val lastName: String?,
    @field:NotBlank(message = "email field is required") @field:Email(message = "email is not valid")
    val email: String?,
    @field:NotNull(message = "gender field is required")
    @field:ListValues(values = ["Masculine", "Feminine"], message = "gender field accept values: Masculine and Feminine")
    val gender: String?
)
