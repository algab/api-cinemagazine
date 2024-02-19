package br.com.cinemagazine.service.impl

import br.com.cinemagazine.constants.ApiMessage.TOKEN_INVALID
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.service.TokenService
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParserBuilder
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtBuilder: JwtBuilder,
    private val jwtParserBuilder: JwtParserBuilder,
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration.access-token}") private val expAccessToken: Long,
    @Value("\${jwt.expiration.refresh-token}") private val expRefreshToken: Long
): TokenService {

    private val accessToken = "access-token"
    private val refreshToken = "refresh-token"
    private val jti = "jti"
    private val firstName = "firstName"
    private val lastName = "lastName"
    private val email = "email"
    private val gender = "gender"
    private val typeToken = "type"

    override fun generateAccessToken(user: UserDTO): String {
        return generateToken(user, accessToken, expAccessToken)
    }

    override fun generateRefreshToken(user: UserDTO): String {
        return generateToken(user, refreshToken, expRefreshToken)
    }

    override fun validateRefreshToken(data: RefreshTokenRequestDTO, agent: String): TokenDTO {
        try {
            val result = refreshTokenRepository.findByToken(data.token!!).orElseThrow {
                throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
            }
            if (result.agent != agent) {
                throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
            }
            val claims = jwtParserBuilder.verifyWith(getSecretKey()).build().parseSignedClaims(data.token)
            if (claims.payload[typeToken].toString() != refreshToken) {
                throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
            }
            val id = claims.payload[jti].toString()
            val firstName = claims.payload[firstName].toString()
            val lastName = claims.payload[lastName].toString()
            val email = claims.payload[email].toString()
            val gender = claims.payload[gender].toString()
            val user = UserDTO(id, firstName, lastName, email, Gender.valueOf(gender))
            return TokenDTO(generateToken(user, accessToken, expAccessToken))
        } catch (exception: Exception) {
            throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
        }
    }

    private fun generateToken(user: UserDTO, type: String, minutes: Long): String {
        return jwtBuilder
            .id(user.id)
            .subject("${user.firstName} ${user.lastName}")
            .claim(firstName, user.firstName)
            .claim(lastName, user.lastName)
            .claim(email, user.email)
            .claim(gender, user.gender)
            .claim(typeToken, type)
            .issuedAt(Date())
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getSecretKey())
            .compact()
    }

    private fun getSecretKey(): SecretKey {
        return hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }
}