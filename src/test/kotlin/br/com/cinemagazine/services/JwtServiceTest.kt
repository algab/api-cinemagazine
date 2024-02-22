package br.com.cinemagazine.services

import br.com.cinemagazine.builder.document.getRefreshTokenDocument
import br.com.cinemagazine.builder.user.getUserDTO
import br.com.cinemagazine.constants.ApiMessage.TOKEN_INVALID
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.services.impl.JwtServiceImpl
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParserBuilder
import io.jsonwebtoken.impl.DefaultClaims
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.Date
import java.util.Optional
import javax.crypto.SecretKey

class JwtServiceTest: FunSpec({

    val refreshTokenRepository = mockk<RefreshTokenRepository>()
    val jwtBuilder = mockk<JwtBuilder>()
    val jwtParserBuilder = mockk<JwtParserBuilder>()
    val service = JwtServiceImpl(
        refreshTokenRepository,
        jwtBuilder,
        jwtParserBuilder,
        "aMH%Q#LHJL@oCWZko@x)+cYk,r:}kHz@T6rlj4st",
        1L,
        2L
    )

    test("should generate access token with successful") {
        every {
            jwtBuilder.id(any(String::class))
                .subject(any(String::class))
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .issuedAt(any(Date::class))
                .expiration(any(Date::class))
                .signWith(any(SecretKey::class))
                .compact()
        } returns "access-token"

        val result = service.generateAccessToken(getUserDTO())

        result.shouldBe("access-token")
    }

    test("should generate refresh token with successful") {
        every {
            jwtBuilder.id(any(String::class))
                .subject(any(String::class))
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .issuedAt(any(Date::class))
                .expiration(any(Date::class))
                .signWith(any(SecretKey::class))
                .compact()
        } returns "refresh-token"

        val result = service.generateRefreshToken(getUserDTO())

        result.shouldBe("refresh-token")
    }

    test("when the token is not saved should return error") {
        every { refreshTokenRepository.findByToken(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            service.validateRefreshToken(RefreshTokenRequestDTO("refresh-token"), "agent")
        }
        exception.message.shouldBe(TOKEN_INVALID.description)
    }

    test("when the user-agent does not match should return error") {
        every { refreshTokenRepository.findByToken(any(String::class)) } returns Optional.of(getRefreshTokenDocument())

        val exception = shouldThrow<BusinessException> {
            service.validateRefreshToken(RefreshTokenRequestDTO("refresh-token"), "agent")
        }
        exception.message.shouldBe(TOKEN_INVALID.description)
    }

    test("when the token type is not equal to refresh-token should return error") {
        val claims = DefaultClaims(hashMapOf("type" to "token"))
        every { refreshTokenRepository.findByToken(any(String::class)) } returns Optional.of(getRefreshTokenDocument())
        every { jwtParserBuilder.verifyWith(any(SecretKey::class)).build().parseSignedClaims(any(String::class)).payload } returns claims

        val exception = shouldThrow<BusinessException> {
            service.validateRefreshToken(RefreshTokenRequestDTO("refresh-token"), "agent")
        }
        exception.message.shouldBe(TOKEN_INVALID.description)
    }

    test("should validate refresh token with successful") {
        val claims = DefaultClaims(hashMapOf(
            "type" to "refresh-token",
            "jit" to "1",
            "firstName" to "Test",
            "lastName" to "test",
            "email" to "test@email.com",
            "gender" to "MASCULINE"
        ))
        every { refreshTokenRepository.findByToken(any(String::class)) } returns Optional.of(getRefreshTokenDocument())
        every { jwtParserBuilder.verifyWith(any(SecretKey::class)).build().parseSignedClaims(any(String::class)).payload } returns claims
        every {
            jwtBuilder.id(any(String::class))
                .subject(any(String::class))
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .claim(any(), any())
                .issuedAt(any(Date::class))
                .expiration(any(Date::class))
                .signWith(any(SecretKey::class))
                .compact()
        } returns "new-token"

        val result = service.validateRefreshToken(RefreshTokenRequestDTO("refresh-token"), "agent")

        result.token.shouldBe("new-token")
    }
})