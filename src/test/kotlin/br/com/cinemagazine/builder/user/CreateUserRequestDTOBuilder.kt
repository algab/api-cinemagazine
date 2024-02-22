package br.com.cinemagazine.builder.user

import br.com.cinemagazine.dto.user.CreateUserRequestDTO

fun getCreateUserRequestDTO(): CreateUserRequestDTO {
    return CreateUserRequestDTO("Test", "Test", "test@email.com", "123456", "Masculine")
}