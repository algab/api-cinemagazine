package br.com.cinemagazine.builder.user

import br.com.cinemagazine.dto.user.LoginRequestDTO

fun getLoginRequestDTO(): LoginRequestDTO {
    return LoginRequestDTO("alvaro@email.com", "123456")
}