package br.com.cinemagazine.service.impl

import br.com.cinemagazine.constants.ApiMessage.TOKEN_INVALID
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
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
    private val jwtBuilder: JwtBuilder,
    private val jwtParserBuilder: JwtParserBuilder,
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.expiration.access-token}") private val expAccessToken: Long,
    @Value("\${jwt.expiration.refresh-token}") private val expRefreshToken: Long
): TokenService {

    override fun generateAccessToken(user: UserDTO): String {
        return generateToken(user, "access-token", expAccessToken)
    }

    override fun generateRefreshToken(user: UserDTO): String {
        return generateToken(user, "refresh-token", expRefreshToken)
    }

    override fun validateRefreshToken(data: RefreshTokenRequestDTO): TokenDTO {
        try {
            val jwtParser = jwtParserBuilder.verifyWith(getSecretKey()).build()
            val jws = jwtParser.parseSignedClaims(data.token)
            if (jws.payload["type"].toString() != "refresh-token") {
                throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
            }
            val id = jws.payload["jti"].toString()
            val firstName = jws.payload["firstName"].toString()
            val lastName = jws.payload["lastName"].toString()
            val email = jws.payload["email"].toString()
            val gender = jws.payload["gender"].toString()
            val user = UserDTO(id, firstName, lastName, email, Gender.valueOf(gender))
            return TokenDTO(generateToken(user, "access-token", expAccessToken))
        } catch (exception: Exception) {
            throw BusinessException(UNAUTHORIZED, TOKEN_INVALID)
        }
    }

    private fun generateToken(user: UserDTO, type: String, minutes: Long): String {
        return jwtBuilder
            .id(user.id)
            .subject("${user.firstName} ${user.lastName}")
            .claim("firstName", user.firstName)
            .claim("lastName", user.lastName)
            .claim("email", user.email)
            .claim("gender", user.gender)
            .claim("type", type)
            .issuedAt(Date())
            .expiration(Date.from(LocalDateTime.now().plusMinutes(minutes).atZone(ZoneId.systemDefault()).toInstant()))
            .signWith(getSecretKey())
            .compact()
    }

    private fun getSecretKey(): SecretKey {
        return hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }
}