package br.com.cinemagazine.builder.user

import br.com.cinemagazine.dto.user.LoginDTO

fun getLoginDTO(): LoginDTO {
    return LoginDTO("access-token", "refresh-token")
}