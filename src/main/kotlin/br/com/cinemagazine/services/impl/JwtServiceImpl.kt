package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.TOKEN_INVALID
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.exception.TokenException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.services.TokenService
import io.jsonwebtoken.Claims
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
    private val keyJti = "jti"
    private val keyFirstName = "firstName"
    private val keyLastName = "lastName"
    private val keyEmail = "email"
    private val keyGender = "gender"
    private val typeToken = "type"

    override fun generateAccessToken(user: UserDTO): String {
        return generateToken(user, accessToken, expAccessToken)
    }

    override fun generateRefreshToken(user: UserDTO): String {
        return generateToken(user, refreshToken, expRefreshToken)
    }

    override fun validateRefreshToken(data: RefreshTokenRequestDTO, agent: String): TokenDTO {
        try {
            val document = refreshTokenRepository.findByToken(data.token!!).orElseThrow {
                throw TokenException()
            }
            if (document.agent != agent) {
                throw TokenException()
            }
            val claims = jwtParserBuilder.verifyWith(getSecretKey()).build().parseSignedClaims(data.token).payload
            if (claims[typeToken].toString() != refreshToken) {
                throw TokenException()
            }
            return TokenDTO(generateToken(mountUser(claims), accessToken, expAccessToken))
        } catch (exception: Exception) {
            throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
        }
    }

    private fun generateToken(user: UserDTO, type: String, minutes: Long): String {
        return jwtBuilder
            .id(user.id)
            .subject("${user.firstName} ${user.lastName}")
            .claim(keyFirstName, user.firstName)
            .claim(keyLastName, user.lastName)
            .claim(keyEmail, user.email)
            .claim(keyGender, user.gender)
            .claim(typeToken, type)
            .issuedAt(Date())
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getSecretKey())
            .compact()
    }

    private fun getSecretKey(): SecretKey {
        return hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }

    private fun mountUser(claims: Claims): UserDTO {
        val id = claims[keyJti].toString()
        val firstName = claims[keyFirstName].toString()
        val lastName = claims[keyLastName].toString()
        val email = claims[keyEmail].toString()
        val gender = claims[keyGender].toString()
        return UserDTO(id, firstName, lastName, email, Gender.valueOf(gender))
    }
}