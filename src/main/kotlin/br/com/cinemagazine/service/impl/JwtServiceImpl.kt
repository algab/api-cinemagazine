package br.com.cinemagazine.service.impl

import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.service.TokenService
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Service
class JwtServiceImpl(
    private val jwtBuilder: JwtBuilder,
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration.access-token}") private val expAccessToken: Long,
    @Value("\${jwt.expiration.refresh-token}") private val expRefreshToken: Long
): TokenService {

    override fun generateAccessToken(user: UserDTO): String {
        return generateToken(user, expAccessToken)
    }

    override fun generateRefreshToken(user: UserDTO): String {
        return generateToken(user, expRefreshToken)
    }

    private fun generateToken(user: UserDTO, minutes: Long): String {
        val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
        return jwtBuilder
            .id(user.id)
            .subject("${user.firstName} ${user.lastName}")
            .issuedAt(Date())
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(key)
            .compact()
    }
}