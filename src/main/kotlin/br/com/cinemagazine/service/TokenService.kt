package br.com.cinemagazine.service

import br.com.cinemagazine.dto.user.UserDTO

interface TokenService {
    fun generateAccessToken(user: UserDTO): String
    fun generateRefreshToken(user: UserDTO): String
}