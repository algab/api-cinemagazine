package br.com.cinemagazine.services

import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.UserDTO

interface TokenService {
    fun generateAccessToken(user: UserDTO): String
    fun generateRefreshToken(user: UserDTO): String
    fun validateAccessToken(token: String)
    fun validateRefreshToken(data: RefreshTokenRequestDTO, agent: String): TokenDTO
    fun getByField(token: String, field: String): String
}