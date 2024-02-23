package br.com.cinemagazine.builder.user

import br.com.cinemagazine.dto.user.UpdateUserRequestDTO

fun getUpdateUserRequestDTO(email: String = "test@email.com", gender: String = "Feminine"): UpdateUserRequestDTO {
    return UpdateUserRequestDTO("Test", "test", email, gender)
}