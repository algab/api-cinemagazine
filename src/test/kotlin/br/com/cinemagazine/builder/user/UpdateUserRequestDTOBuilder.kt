package br.com.cinemagazine.builder.user

import br.com.cinemagazine.dto.user.UpdateUserRequestDTO

fun getUpdateUserRequestDTO(): UpdateUserRequestDTO {
    return UpdateUserRequestDTO("Test", "test", "test@email.com", "Feminine")
}