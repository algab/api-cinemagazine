package br.com.cinemagazine.builder.user

import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.constants.Gender.MASCULINE
import br.com.cinemagazine.dto.user.UserDTO

fun getUserDTO(gender: Gender = MASCULINE): UserDTO {
    return UserDTO("1", "Test", "Test", "test@email.com", gender)
}