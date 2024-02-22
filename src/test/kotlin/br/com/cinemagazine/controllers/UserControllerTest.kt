package br.com.cinemagazine.controllers

import br.com.cinemagazine.builder.token.getRefreshTokenRequestDTO
import br.com.cinemagazine.builder.token.getTokenDTO
import br.com.cinemagazine.builder.user.getCreateUserRequestDTO
import br.com.cinemagazine.builder.user.getLoginDTO
import br.com.cinemagazine.builder.user.getLoginRequestDTO
import br.com.cinemagazine.builder.user.getUpdatePasswordRequestDTO
import br.com.cinemagazine.builder.user.getUpdateUserRequestDTO
import br.com.cinemagazine.builder.user.getUserDTO
import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.dto.token.RefreshTokenRequestDTO
import br.com.cinemagazine.dto.user.CreateUserRequestDTO
import br.com.cinemagazine.dto.user.LoginRequestDTO
import br.com.cinemagazine.dto.user.UpdatePasswordRequestDTO
import br.com.cinemagazine.dto.user.UpdateUserRequestDTO
import br.com.cinemagazine.services.TokenService
import br.com.cinemagazine.services.UserService
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
        every { userService.login(any(LoginRequestDTO::class), any(String::class)) } returns getLoginDTO()

        val result = userController.login(getLoginRequestDTO(), "user-agent")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.accessToken.shouldBe(getLoginDTO().accessToken)
        result.body?.refreshToken.shouldBe(getLoginDTO().refreshToken)
    }

    test("should execute refresh-token with successful") {
        every { tokenService.validateRefreshToken(any(RefreshTokenRequestDTO::class), any(String::class)) } returns getTokenDTO()

        val result = userController.refreshToken(getRefreshTokenRequestDTO(), "user-agent")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.token.shouldBe(getTokenDTO().token)
    }

    test("should create user with successful") {
        val user = getUserDTO()
        every { userService.createUser(any(CreateUserRequestDTO::class)) } returns user

        val result = userController.createUser(getCreateUserRequestDTO())

        result.statusCode.shouldBe(HttpStatus.CREATED)
        result.body?.id.shouldBe(user.id)
        result.body?.firstName.shouldBe(user.firstName)
        result.body?.lastName.shouldBe(user.lastName)
        result.body?.email.shouldBe(user.email)
        result.body?.gender.shouldBe(user.gender)
    }

    test("should get user with successful") {
        val user = getUserDTO()
        every { userService.getUser(any(String::class)) } returns user

        val result = userController.getUser("1")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.id.shouldBe(user.id)
        result.body?.firstName.shouldBe(user.firstName)
        result.body?.lastName.shouldBe(user.lastName)
        result.body?.email.shouldBe(user.email)
        result.body?.gender.shouldBe(user.gender)
    }

    test("should update user with successful") {
        val user = getUserDTO(Gender.FEMININE)
        every { userService.updateUser(any(String::class), any(UpdateUserRequestDTO::class)) } returns user

        val result = userController.updateUser("1", getUpdateUserRequestDTO())

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.id.shouldBe(user.id)
        result.body?.firstName.shouldBe(user.firstName)
        result.body?.lastName.shouldBe(user.lastName)
        result.body?.email.shouldBe(user.email)
        result.body?.gender.shouldBe(user.gender)
    }

    test("should update password with successful") {
        every { userService.updatePassword(any(String::class), any(UpdatePasswordRequestDTO::class)) } returns getUserDTO(Gender.FEMININE)

        val result = userController.updatePassword("1", getUpdatePasswordRequestDTO())

        result.statusCode.shouldBe(HttpStatus.OK)
    }

    test("should delete user with successful") {
        every { userService.deleteUser(any(String::class)) } returns Unit

        val result = userController.deleteUser("1")

        result.statusCode.shouldBe(HttpStatus.NO_CONTENT)
    }
})