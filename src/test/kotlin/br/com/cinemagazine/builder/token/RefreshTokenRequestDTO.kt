package br.com.cinemagazine.builder.token

import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO

fun getRefreshTokenRequestDTO(): RefreshTokenRequestDTO {
    return RefreshTokenRequestDTO("refresh-token")
}