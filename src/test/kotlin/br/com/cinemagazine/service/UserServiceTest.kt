package br.com.cinemagazine.service

import br.com.cinemagazine.constants.ApiMessage
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.document.RefreshTokenDocument
import br.com.cinemagazine.document.UserDocument
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.repository.UserRepository
import br.com.cinemagazine.service.impl.UserServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.*

class UserServiceTest: FunSpec({

    val tokenService = mockk<TokenService>()
    val userRepository = mockk<UserRepository>()
    val refreshTokenRepository = mockk<RefreshTokenRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userService = UserServiceImpl(tokenService, userRepository, refreshTokenRepository, passwordEncoder)

    test("should execute login with successful") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        every { userRepository.findByEmail(any()) } returns Optional.of(userDocument)
        every { passwordEncoder.matches(any(), any()) } returns true
        every { tokenService.generateAccessToken(any()) } returns "access-token"
        every { tokenService.generateRefreshToken(any()) } returns "refresh-token"
        every { refreshTokenRepository.save(any()) } returns RefreshTokenDocument("1", "refresh-token", "agent", LocalDateTime.now())

        val result = userService.login(LoginRequestDTO("alvaro@email.com", "123456"), "user-agent")

        result.accessToken.shouldBe("access-token")
        result.refreshToken.shouldBe("refresh-token")
    }

    test("should execute login return email not found") {
        every { userRepository.findByEmail(any()) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.login(LoginRequestDTO("alvaro@email.com", "123456"), "user-agent")
        }
        exception.message.shouldBe(ApiMessage.USER_NOT_FOUND.description)
    }

    test("should execute login return password not matches") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        every { userRepository.findByEmail(any()) } returns Optional.of(userDocument)
        every { passwordEncoder.matches(any(), any()) } returns false

        val exception = shouldThrow<BusinessException> {
            userService.login(LoginRequestDTO("alvaro@email.com", "123456"), "user-agent")
        }
        exception.message.shouldBe(ApiMessage.USER_NOT_FOUND.description)
    }

    test("should create user with successful") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        every { userRepository.existsByEmail(any()) } returns false
        every { passwordEncoder.encode(any()) } returns "1234"
        every { userRepository.save(any()) } returns userDocument

        val result = userService.createUser(CreateUserRequestDTO("Test", "Test", "test@email.com", "123456", "Masculine"))

        result.firstName.shouldBe("Test")
    }

    test("should create user return email invalid") {
        every { userRepository.existsByEmail(any()) } returns true

        val exception = shouldThrow<BusinessException> {
            userService.createUser(CreateUserRequestDTO("Test", "Test", "test@email.com", "123456", "Masculine"))
        }
        verify { userRepository.save(any()) wasNot Called }
        exception.message.shouldBe(ApiMessage.EMAIL_ALREADY_EXISTS.description)
    }

    test("should get user with successful") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        every { userRepository.findById(any()) } returns Optional.of(userDocument)

        val result = userService.getUser("1")

        result.firstName.shouldBe(userDocument.firstName)
        result.lastName.shouldBe(userDocument.lastName)
    }

    test("should get user return error") {
        every { userRepository.findById(any()) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.getUser("1")
        }
        exception.message.shouldBe(ApiMessage.USER_NOT_FOUND.description)
    }

    test("should update user with successful") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        val userUpdated = userDocument.copy()
        userUpdated.gender = Gender.FEMININE
        every { userRepository.findById(any()) } returns Optional.of(userDocument)
        every { userRepository.save(any()) } returns userUpdated

        val result = userService.updateUser("1", UpdateUserRequestDTO("Test", "test", "test@email.com", "Feminine"))

        result.gender.shouldBe(Gender.FEMININE)
    }

    test("should update user with successful with different email") {
        val userDocument = UserDocument("1", "Test", "Test", "test@email.com", "1234", Gender.MASCULINE, LocalDateTime.now(), LocalDateTime.now())
        val userUpdated = userDocument.copy()
        userUpdated.email = "test10@email.com"
        userUpdated.gender = Gender.FEMININE
        every { userRepository.findById(any()) } returns Optional.of(userDocument)
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.save(any()) } returns userUpdated

        val result = userService.updateUser("1", UpdateUserRequestDTO("Test", "test", "test10@email.com", "Feminine"))

        result.email.shouldBe("test10@email.com")
        result.gender.shouldBe(Gender.FEMININE)
    }

    test("should update user return error") {
        every { userRepository.findById(any()) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.updateUser("1", UpdateUserRequestDTO("Test", "test", "test10@email.com", "Feminine"))
        }
        exception.message.shouldBe(ApiMessage.USER_NOT_FOUND.description)
    }
})