package br.com.cinemagazine.controller

import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.token.TokenDTO
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.dto.user.UserDTO
import br.com.cinemagazine.service.TokenService
import br.com.cinemagazine.service.UserService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class UserControllerTest: FunSpec({

    val userService = mockk<UserService>()
    val tokenService = mockk<TokenService>()
    val userController = UserController(userService, tokenService)

    test("should execute login with successful") {
        every { userService.login( any(), any()) } returns LoginDTO("access-token", "refresh-token")

        val result = userController.login(LoginRequestDTO("alvaro@email.com", "123456"), "user-agent")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.accessToken.shouldBe("access-token")
        result.body?.refreshToken.shouldBe("refresh-token")
    }

    test("should execute refresh-token with successful") {
        every { tokenService.validateRefreshToken(any(), any()) } returns TokenDTO("new-token")

        val result = userController.refreshToken(RefreshTokenRequestDTO("refresh-token"), "user-agent")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.token.shouldBe("new-token")
    }

    test("should create user with successful") {
        every { userService.createUser(any()) } returns UserDTO("1", "Test", "Test", "test@email.com", Gender.MASCULINE)

        val result = userController.createUser(CreateUserRequestDTO("Test", "Test", "test@email.com", "123456", "Masculine"))

        result.statusCode.shouldBe(HttpStatus.CREATED)
        result.body?.firstName.shouldBe("Test")
    }

    test("should get user with successful") {
        every { userService.getUser(any()) } returns UserDTO("1", "Test", "Test", "test@email.com", Gender.MASCULINE)

        val result = userController.getUser("1")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.firstName.shouldBe("Test")
    }

    test("should update user with successful") {
        every { userService.updateUser(any(), any()) } returns UserDTO("1", "Test", "Test", "test@email.com", Gender.FEMININE)

        val result = userController.updateUser("1", UpdateUserRequestDTO("Test", "test", "test@email.com", "Feminine"))

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.gender.shouldBe(Gender.FEMININE)
    }

    test("should update password with successful") {
        every { userService.updatePassword(any(), any()) } returns UserDTO("1", "Test", "Test", "test@email.com", Gender.FEMININE)

        val result = userController.updatePassword("1", UpdatePasswordRequestDTO("12345678"))

        result.statusCode.shouldBe(HttpStatus.OK)
    }

    test("should delete user with successful") {
        every { userService.deleteUser(any()) } returns Unit

        val result = userController.deleteUser("1")

        result.statusCode.shouldBe(HttpStatus.NO_CONTENT)
    }
})