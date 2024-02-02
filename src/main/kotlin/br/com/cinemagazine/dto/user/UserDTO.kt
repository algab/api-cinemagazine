package br.com.cinemagazine.dto.user

import br.com.cinemagazine.constants.Gender

data class UserDTO(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val gender: Gender
)
