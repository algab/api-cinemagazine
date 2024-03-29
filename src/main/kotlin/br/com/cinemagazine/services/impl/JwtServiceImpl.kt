package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.REFRESH_TOKEN_NOT_FOUND
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
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(this.javaClass)

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

    override fun validateAccessToken(token: String) {
        try {
            val claims = jwtParserBuilder.verifyWith(getSecretKey()).build().parseSignedClaims(token).payload
            if (claims[typeToken].toString() != accessToken) {
                logger.debug("JwtServiceImpl.validateAccessToken - Type token incorrect")
                throw TokenException()
            }
        } catch (exception: Exception) {
            logger.error("JwtServiceImpl.validateAccessToken - Input: token [{}] - {}", token, TOKEN_INVALID.description)
            throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
        }
    }

    override fun validateRefreshToken(data: RefreshTokenRequestDTO, agent: String): TokenDTO {
        try {
            val document = refreshTokenRepository.findByToken(data.token!!).orElseThrow {
                logger.debug("JwtServiceImpl.validateRefreshToken - {}", REFRESH_TOKEN_NOT_FOUND.description)
                throw TokenException()
            }
            if (document.agent != agent) {
                logger.debug("JwtServiceImpl.validateRefreshToken - User agent is different")
                throw TokenException()
            }
            val claims = jwtParserBuilder.verifyWith(getSecretKey()).build().parseSignedClaims(data.token).payload
            if (claims[typeToken].toString() != refreshToken) {
                logger.debug("JwtServiceImpl.validateRefreshToken - Type token incorrect")
                throw TokenException()
            }
            return TokenDTO(generateToken(mountUser(claims), accessToken, expAccessToken))
        } catch (exception: Exception) {
            logger.error("JwtServiceImpl.validateRefreshToken - Input: data [{}], agent [{}] - {}", data, agent, TOKEN_INVALID.description)
            throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
        }
    }

    override fun getByField(token: String, field: String): String {
        try {
            val claims = jwtParserBuilder.verifyWith(getSecretKey()).build().parseSignedClaims(token).payload
            val value = claims[field].toString()
            if (value == "null") {
                logger.debug("JwtServiceImpl.getByField - Field not found")
                throw TokenException()
            }
            return value
        } catch (exception: Exception) {
            logger.error("JwtServiceImpl.getByField - Input: token [{}] - {}", token, TOKEN_INVALID.description)
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