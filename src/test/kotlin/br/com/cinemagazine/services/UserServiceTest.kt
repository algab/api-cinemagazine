package br.com.cinemagazine.services

import br.com.cinemagazine.builder.document.getRefreshTokenDocument
import br.com.cinemagazine.builder.document.getUserDocument
import br.com.cinemagazine.builder.user.getCreateUserRequestDTO
import br.com.cinemagazine.builder.user.getLoginRequestDTO
import br.com.cinemagazine.builder.user.getUpdatePasswordRequestDTO
import br.com.cinemagazine.builder.user.getUpdateUserRequestDTO
import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.constants.ApiMessage.USER_NOT_FOUND
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.documents.RefreshTokenDocument
import br.com.cinemagazine.documents.UserDocument
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.RefreshTokenRepository
import br.com.cinemagazine.repository.UserRepository
import br.com.cinemagazine.services.impl.UserServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

class UserServiceTest: FunSpec({

    val tokenService = mockk<TokenService>()
    val userRepository = mockk<UserRepository>()
    val refreshTokenRepository = mockk<RefreshTokenRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val userService = UserServiceImpl(tokenService, userRepository, refreshTokenRepository, passwordEncoder)

    test("should execute login with successful") {
        every { userRepository.findByEmail(any(String::class)) } returns Optional.of(getUserDocument())
        every { passwordEncoder.matches(any(), any()) } returns true
        every { tokenService.generateAccessToken(any(UserDTO::class)) } returns "access-token"
        every { tokenService.generateRefreshToken(any(UserDTO::class)) } returns "refresh-token"
        every { refreshTokenRepository.save(any(RefreshTokenDocument::class)) } returns getRefreshTokenDocument()

        val result = userService.login(getLoginRequestDTO(), "user-agent")

        result.accessToken.shouldBe("access-token")
        result.refreshToken.shouldBe("refresh-token")
    }

    test("when the email does not exist should return error") {
        every { userRepository.findByEmail(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.login(getLoginRequestDTO(), "user-agent")
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }

    test("when the password is wrong should return an error") {
        every { userRepository.findByEmail(any(String::class)) } returns Optional.of(getUserDocument())
        every { passwordEncoder.matches(any(), any()) } returns false

        val exception = shouldThrow<BusinessException> {
            userService.login(LoginRequestDTO("alvaro@email.com", "123456"), "user-agent")
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }

    test("should create user with successful") {
        val userDocument = getUserDocument()
        every { userRepository.existsByEmail(any(String::class)) } returns false
        every { passwordEncoder.encode(any(String::class)) } returns "1234"
        every { userRepository.save(any()) } returns userDocument

        val result = userService.createUser(getCreateUserRequestDTO())

        result.id.shouldBe(userDocument.id)
        result.firstName.shouldBe(userDocument.firstName)
        result.lastName.shouldBe(userDocument.lastName)
        result.email.shouldBe(userDocument.email)
        result.gender.shouldBe(userDocument.gender)
    }

    test("when an email already exists should return an error") {
        every { userRepository.existsByEmail(any(String::class)) } returns true

        val exception = shouldThrow<BusinessException> {
            userService.createUser(getCreateUserRequestDTO())
        }
        exception.message.shouldBe(EMAIL_ALREADY_EXISTS.description)
        verify { userRepository.save(any()) wasNot Called }
    }

    test("should get user with successful") {
        val userDocument = getUserDocument()
        every { userRepository.findById(any(String::class)) } returns Optional.of(userDocument)

        val result = userService.getUser("1")

        result.id.shouldBe(userDocument.id)
        result.firstName.shouldBe(userDocument.firstName)
        result.lastName.shouldBe(userDocument.lastName)
        result.email.shouldBe(userDocument.email)
        result.gender.shouldBe(userDocument.gender)
    }

    test("when the user id does not exist should return error in getUser") {
        every { userRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.getUser("1")
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }

    test("should update user with successful") {
        val userDocument = getUserDocument()
        val userUpdated = userDocument.copy()
        userUpdated.firstName = "User"
        userUpdated.gender = Gender.FEMININE
        every { userRepository.findById(any(String::class)) } returns Optional.of(userDocument)
        every { userRepository.save(any(UserDocument::class)) } returns userUpdated

        val result = userService.updateUser("1", getUpdateUserRequestDTO())

        result.id.shouldBe(userUpdated.id)
        result.firstName.shouldBe(userUpdated.firstName)
        result.lastName.shouldBe(userUpdated.lastName)
        result.email.shouldBe(userUpdated.email)
        result.gender.shouldBe(userUpdated.gender)
    }

    test("should update user with successful with different email") {
        val userDocument = getUserDocument()
        val userUpdated = userDocument.copy()
        userUpdated.email = "test10@email.com"
        userUpdated.gender = Gender.FEMININE
        every { userRepository.findById(any(String::class)) } returns Optional.of(userDocument)
        every { userRepository.existsByEmail(any(String::class)) } returns false
        every { userRepository.save(any(UserDocument::class)) } returns userUpdated

        val result = userService.updateUser("1", getUpdateUserRequestDTO("test10@email.com"))

        result.id.shouldBe(userUpdated.id)
        result.firstName.shouldBe(userUpdated.firstName)
        result.lastName.shouldBe(userUpdated.lastName)
        result.email.shouldBe(userUpdated.email)
        result.gender.shouldBe(userUpdated.gender)
    }

    test("when the user id does not exist should return error in updateUser") {
        every { userRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.updateUser("1", getUpdateUserRequestDTO())
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }

    test("should update password with successful") {
        val userDocument = getUserDocument()
        every { userRepository.findById(any(String::class)) } returns Optional.of(userDocument)
        every { passwordEncoder.encode(any(String::class)) } returns "1234"
        every { userRepository.save(any(UserDocument::class)) } returns userDocument

        val result = userService.updatePassword("1", getUpdatePasswordRequestDTO())

        result.id.shouldBe(userDocument.id)
        result.firstName.shouldBe(userDocument.firstName)
        result.lastName.shouldBe(userDocument.lastName)
        result.email.shouldBe(userDocument.email)
        result.gender.shouldBe(userDocument.gender)
    }

    test("when the user id does not exist should return error in updatePassword") {
        every { userRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.updatePassword("1", getUpdatePasswordRequestDTO())
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }

    test("should delete user with successful") {
        every { userRepository.findById(any(String::class)) } returns Optional.of(getUserDocument())
        every { userRepository.delete(any(UserDocument::class)) } returns Unit

        userService.deleteUser("1")

        verify(exactly = 1) { userRepository.delete(any(UserDocument::class)) }
    }

    test("when the user id does not exist should return error in deleteUser") {
        every { userRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            userService.deleteUser("1")
        }
        exception.message.shouldBe(USER_NOT_FOUND.description)
    }
})